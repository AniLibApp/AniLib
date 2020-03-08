package com.revolgenx.anilib.activity

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.event.meta.BrowseMediaMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.browser.*
import kotlinx.android.synthetic.main.activity_media_browser.*
import kotlinx.android.synthetic.main.smart_tab_layout.view.*
import java.util.*

class MediaBrowserActivity : DynamicSystemActivity() {

    companion object {
        const val MEDIA_BROWSER_META = "media_browser_meta"
    }

    private lateinit var tabColorStateList: ColorStateList
    private val pageChangeListener by lazy {
        object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                smartTabLayout.getTabs().forEach { it.tabTextTv.visibility = View.GONE }
                smartTabLayout.getTabAt(position).tabTextTv.visibility = View.VISIBLE
            }
        }
    }

    private val accentColor by lazy {
        DynamicTheme.getInstance().get().accentColor
    }


    private val states = arrayOf(
        intArrayOf(android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_enabled)
    )

    override fun getLocale(): Locale? {
        return null
    }


    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.postponeEnterTransition(this)
        setContentView(R.layout.activity_media_browser)

        val browserMeta: BrowseMediaMeta = intent.getParcelableExtra(MEDIA_BROWSER_META) ?: return

        val colors = intArrayOf(
            DynamicTheme.getInstance().get().accentColor,
            DynamicTheme.getInstance().get().tintPrimaryColor
        )

        tabColorStateList = ColorStateList(states, colors)
        browserRootLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        smartTabLayout.setBackgroundColor(DynamicTheme.getInstance().get().primaryColor)
        statusBarColor = statusBarColor

        val inflater = LayoutInflater.from(this)

        smartTabLayout.setCustomTabView { container, position, adapter ->
            val view = inflater.inflate(R.layout.smart_tab_layout, container, false)
            when (position) {
                0 -> {
                    createTabView(view, R.drawable.ic_overview, R.string.overview)
                }
                1 -> {
                    createTabView(view, R.drawable.ic_watch, R.string.watch)
                }
                2 -> {
                    createTabView(view, R.drawable.ic_character, R.string.character)
                }
                3 -> {
                    createTabView(view, R.drawable.ic_staff, R.string.staff)
                }
                4 -> {
                    createTabView(view, R.drawable.ic_review, R.string.review)
                }
                5 -> {
                    createTabView(view, R.drawable.ic_chart, R.string.stats)
                }
                else -> {
                    null
                }
            }
        }
        browseMediaViewPager.addOnPageChangeListener(pageChangeListener)

        val animeBrowserList = listOf(
            BaseFragment.newInstance(MediaOverviewFragment::class.java).apply {
                arguments = bundleOf(MEDIA_BROWSER_META to browserMeta)
            },
            BaseFragment.newInstance(MediaWatchFragment::class.java).apply {
                arguments = bundleOf(MEDIA_BROWSER_META to browserMeta)
            },
            BaseFragment.newInstance(MediaCharacterFragment::class.java).apply {
                arguments = bundleOf(MEDIA_BROWSER_META to browserMeta)
            },
            BaseFragment.newInstance(MediaStaffFragment::class.java).apply {
                arguments = bundleOf(MEDIA_BROWSER_META to browserMeta)
            },
            BaseFragment.newInstance(MediaReviewFragment::class.java).apply {
                arguments = bundleOf(MEDIA_BROWSER_META to browserMeta)
            },
            BaseFragment.newInstance(MediaStatsFragment::class.java).apply {
                arguments = bundleOf(MEDIA_BROWSER_META to browserMeta)
            }
        )

        browseMediaViewPager.adapter = MediaBrowserAdapter(animeBrowserList)
        smartTabLayout.setViewPager(browseMediaViewPager)
        browseMediaViewPager.setCurrentItem(0, false)
        browseMediaViewPager.post {
            pageChangeListener.onPageSelected(browseMediaViewPager.currentItem)
            ActivityCompat.startPostponedEnterTransition(this@MediaBrowserActivity);
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

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }


    inner class MediaBrowserAdapter(private val fragmentList: List<BaseFragment>) :
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

