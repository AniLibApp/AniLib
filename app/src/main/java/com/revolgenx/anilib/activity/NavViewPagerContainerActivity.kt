package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.NavViewPagerParcelableFragments
import com.revolgenx.anilib.ui.fragment.stats.*
import com.revolgenx.anilib.data.meta.NavViewPagerContainerMeta
import com.revolgenx.anilib.data.meta.NavViewPagerContainerType
import com.revolgenx.anilib.databinding.NavViewPagerContainerActivityLayoutBinding
import kotlinx.android.synthetic.main.custom_bottom_navigation_view.*
import kotlinx.android.synthetic.main.nav_view_pager_container_activity_layout.*
import kotlinx.android.synthetic.main.smart_tab_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class NavViewPagerContainerActivity : BaseDynamicActivity<NavViewPagerContainerActivityLayoutBinding>() {

    companion object {
        const val NavViewPagerContainerMetaKey = "NavviewPagerContainerMetaKey"

        fun <T : Parcelable> openActivity(
            context: Context,
            meta: NavViewPagerContainerMeta<T>
        ) {
            context.startActivity(Intent(context, NavViewPagerContainerActivity::class.java).also {
                it.putExtra(NavViewPagerContainerMetaKey, meta)
            })
        }
    }

    private val tabColorStateList: ColorStateList
        get() {
            return ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_selected),
                    intArrayOf(android.R.attr.state_enabled)
                )
                , intArrayOf(
                    DynamicTheme.getInstance().get().accentColor,
                    DynamicTheme.getInstance().get().tintPrimaryColor
                )
            )
        }


    private val accentColor by lazy {
        DynamicTheme.getInstance().get().accentColor
    }
    private val tintAccentColor by lazy {
        DynamicTheme.getInstance().get().tintAccentColor
    }

    private val pageChangeListener by lazy {
        object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                base_dynamic_smart_tab.getTabs().forEach { it.tab_text_tv.visibility = View.GONE }
                base_dynamic_smart_tab.getTabAt(position).tab_text_tv.visibility = View.VISIBLE
            }
        }
    }

    private lateinit var viewPagerParcelableFragments: NavViewPagerParcelableFragments
    private lateinit var viewPagerMeta: NavViewPagerContainerMeta<Parcelable>

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NavViewPagerContainerActivityLayoutBinding {
        return NavViewPagerContainerActivityLayoutBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dynamicToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        base_dynamic_smart_tab.setBackgroundColor(DynamicTheme.getInstance().get().primaryColor)

        viewPagerMeta = intent.getParcelableExtra(NavViewPagerContainerMetaKey) ?: return
        prepareViews(viewPagerMeta)

        navViewPager.addOnPageChangeListener(pageChangeListener)
        navViewPager.adapter = ViewPagerContainerAdapter()
        navViewPager.offscreenPageLimit = viewPagerParcelableFragments.clzzes.size - 1
        base_dynamic_smart_tab.setViewPager(navViewPager, null)
        navViewPager.setCurrentItem(0, false)
        navViewPager.post {
            pageChangeListener.onPageSelected(navViewPager.currentItem)
        }

    }


    private fun prepareViews(viewPagerMeta: NavViewPagerContainerMeta<Parcelable>) {
        when (viewPagerMeta.containerType) {
            NavViewPagerContainerType.ANIME_STATS -> {
                supportActionBar?.title = getString(R.string.anime_stats)
                inflateBottomNav(viewPagerMeta.containerType)
                viewPagerParcelableFragments = NavViewPagerParcelableFragments(
                    listOf(
                        StatsOverviewFragment::class.java.name,
                        StatsGenreFragment::class.java.name,
                        StatsTagFragment::class.java.name,
                        StatsVoiceActorFragment::class.java.name,
                        StatsStudioFragment::class.java.name,
                        StatsStaffFragment::class.java.name
                    ),
                    listOf(
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        )
                    )
                )
            }
            NavViewPagerContainerType.MANGA_STATS -> {
                supportActionBar?.title = getString(R.string.manga_stats)
                inflateBottomNav(viewPagerMeta.containerType)
                viewPagerParcelableFragments = NavViewPagerParcelableFragments(
                    listOf(
                        StatsOverviewFragment::class.java.name,
                        StatsGenreFragment::class.java.name,
                        StatsTagFragment::class.java.name,
                        StatsStaffFragment::class.java.name
                    ),
                    listOf(
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        ),
                        bundleOf(
                            StatsOverviewFragment.USER_STATS_PARCEL_KEY to viewPagerMeta.data
                        )
                    )
                )
            }
        }
    }

    private fun inflateBottomNav(containerType: NavViewPagerContainerType) {
        when (containerType) {
            NavViewPagerContainerType.ANIME_STATS -> {
                val inflater = LayoutInflater.from(this)
                base_dynamic_smart_tab.setCustomTabView { container, position, _ ->
                    val view = inflater.inflate(R.layout.smart_tab_layout, container, false)
                    when (position) {
                        0 -> {
                            createTabView(view, R.drawable.ic_fire, R.string.overview)
                        }
                        1 -> {
                            createTabView(view, R.drawable.ic_genre, R.string.genre)
                        }
                        2 -> {
                            createTabView(view, R.drawable.ic_tag, R.string.tags)
                        }
                        3 -> {
                            createTabView(view, R.drawable.ic_voice_role, R.string.voice_actors)
                        }
                        4 -> {
                            createTabView(view, R.drawable.ic_studio, R.string.studios)
                        }
                        5 -> {
                            createTabView(view, R.drawable.ic_staff, R.string.staff)
                        }
                        else -> {
                            null
                        }
                    }
                }
            }
            NavViewPagerContainerType.MANGA_STATS -> {
                val inflater = LayoutInflater.from(this)
                base_dynamic_smart_tab.setCustomTabView { container, position, _ ->
                    val view = inflater.inflate(R.layout.smart_tab_layout, container, false)
                    when (position) {
                        0 -> {
                            createTabView(view, R.drawable.ic_fire, R.string.overview)
                        }
                        1 -> {
                            createTabView(view, R.drawable.ic_genre, R.string.genre)
                        }
                        2 -> {
                            createTabView(view, R.drawable.ic_tag, R.string.tags)
                        }
                        3 -> {
                            createTabView(view, R.drawable.ic_staff, R.string.staff)
                        }
                        else -> {
                            null
                        }
                    }
                }
            }
        }
    }

    private fun createTabView(view: View, @DrawableRes src: Int, @StringRes str: Int): View {
        view.tab_image_view.imageTintList = tabColorStateList
        view.tab_image_view.setImageResource(src)
        view.tab_text_tv.text = getString(str)
        view.background = RippleDrawable(ColorStateList.valueOf(tintAccentColor), null, null)
        view.tab_text_tv.setTextColor(accentColor)
        return view
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }



    inner class ViewPagerContainerAdapter :
        FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return (Class.forName(viewPagerParcelableFragments.clzzes[position]).newInstance() as BaseFragment).also {
                it.arguments = viewPagerParcelableFragments.bundles[position]
            }
        }

        override fun getCount(): Int {
            return viewPagerParcelableFragments.clzzes.size
        }
    }
}