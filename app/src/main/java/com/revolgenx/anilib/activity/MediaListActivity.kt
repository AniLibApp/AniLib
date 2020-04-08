package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.dialog.MediaListFilterDialog
import com.revolgenx.anilib.event.meta.MediaListMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.list.*
import com.revolgenx.anilib.type.MediaType
import kotlinx.android.synthetic.main.media_list_activity_layout.*
import kotlinx.android.synthetic.main.smart_tab_layout.view.*
import java.util.*

class MediaListActivity : DynamicSystemActivity() {

    companion object {
        fun openActivity(context: Context, mediaListMeta: MediaListMeta) {
            context.startActivity(Intent(context, MediaListActivity::class.java).also {
                it.putExtra(MEDIA_LIST_META_KEY, mediaListMeta)
            })
        }

        const val MEDIA_LIST_META_KEY = "MEDIA_LIST_INTENT_KEY"
    }

    override fun getLocale(): Locale? {
        return null
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }


    private val mediaListFragment by lazy {
        listOf(
            BaseFragment.newInstance(WatchingFragment::class.java).apply {
                arguments = bundleOf(MEDIA_LIST_META_KEY to mediaListMeta)
            },
            BaseFragment.newInstance(PlanningFragment::class.java).apply {
                arguments = bundleOf(MEDIA_LIST_META_KEY to mediaListMeta)
            },
            BaseFragment.newInstance(CompletedFragment::class.java).apply {
                arguments = bundleOf(MEDIA_LIST_META_KEY to mediaListMeta)
            },
            BaseFragment.newInstance(DroppedFragment::class.java).apply {
                arguments = bundleOf(MEDIA_LIST_META_KEY to mediaListMeta)
            },
            BaseFragment.newInstance(PausedFragment::class.java).apply {
                arguments = bundleOf(MEDIA_LIST_META_KEY to mediaListMeta)
            },
            BaseFragment.newInstance(RepeatingFragment::class.java).apply {
                arguments = bundleOf(MEDIA_LIST_META_KEY to mediaListMeta)
            }
        )
    }

    private val pageChangeListener by lazy {
        object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                listSmartTab.getTabs().forEach { it.tabTextTv.visibility = View.GONE }
                listSmartTab.getTabAt(position).tabTextTv.visibility = View.VISIBLE
            }
        }
    }


    private lateinit var tabColorStateList: ColorStateList
    private lateinit var mediaListMeta: MediaListMeta
    private val accentColor by lazy {
        DynamicTheme.getInstance().get().accentColor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_list_activity_layout)
        listRootLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)

        val colors = intArrayOf(
            DynamicTheme.getInstance().get().accentColor,
            DynamicTheme.getInstance().get().tintPrimaryColor
        )

        setSupportActionBar(listToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        listSmartTab.setBackgroundColor(DynamicTheme.getInstance().get().primaryColor)
        statusBarColor = statusBarColor

        mediaListMeta = intent.getParcelableExtra(MEDIA_LIST_META_KEY) ?: return
        if ((mediaListMeta.userId == null && mediaListMeta.userName == null)) {
            return
        }
        supportActionBar!!.title = when (mediaListMeta.type) {
            MediaType.ANIME.ordinal -> {
                getString(R.string.anime_list)
            }
            else -> {
                getString(R.string.manga_list)
            }
        }

        tabColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_enabled)
            )
            , colors
        )

        val inflater = LayoutInflater.from(this)
        listSmartTab.setCustomTabView { container, position, adapter ->
            val view = inflater.inflate(R.layout.smart_tab_layout, container, false)
            when (position) {
                0 -> {
                    createTabView(view, R.drawable.ic_watching, R.string.watching)
                }
                1 -> {
                    createTabView(view, R.drawable.ic_planning, R.string.planning)
                }
                2 -> {
                    createTabView(view, R.drawable.ic_completed, R.string.completed)
                }
                3 -> {
                    createTabView(view, R.drawable.ic_dropped, R.string.dropped)
                }
                4 -> {
                    createTabView(view, R.drawable.ic_paused_filled, R.string.paused)
                }
                5 -> {
                    createTabView(view, R.drawable.ic_rewatching, R.string.repeating)
                }
                else -> {
                    null
                }
            }
        }

        mediaListViewPager.addOnPageChangeListener(pageChangeListener)
        mediaListViewPager.adapter = MediaListAdapter(mediaListFragment)
        mediaListViewPager.offscreenPageLimit = 5
        listSmartTab.setViewPager(mediaListViewPager, null)
        mediaListViewPager.setCurrentItem(0, false)
        mediaListViewPager.post {
            pageChangeListener.onPageSelected(mediaListViewPager.currentItem)
        }


    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        menuInflater.inflate(R.menu.list_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listSearchMenu -> {
                true
            }
            R.id.listFilterMenu -> {
                MediaListFilterDialog.newInstance()
                    .show(supportFragmentManager, "media_filter_dialog")
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun createTabView(view: View, @DrawableRes src: Int, @StringRes str: Int): View {
        view.tabImageView.imageTintList = tabColorStateList
        view.tabImageView.setImageResource(src)
        view.tabTextTv.text = getString(str)
        view.background = RippleDrawable(ColorStateList.valueOf(accentColor), null, null)
        view.tabTextTv.setTextColor(accentColor)
        return view
    }

    inner class MediaListAdapter(private val fragmentList: List<BaseFragment>) :
        FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }


}