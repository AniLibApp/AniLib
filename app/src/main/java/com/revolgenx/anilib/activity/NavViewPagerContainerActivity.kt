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
import com.revolgenx.anilib.databinding.SmartTabLayoutBinding

class NavViewPagerContainerActivity : BaseDynamicActivity<NavViewPagerContainerActivityLayoutBinding>() {

    companion object {
        const val NavViewPagerContainerMetaKey = "NavviewPagerContainerMetaKey"

        fun openActivity(
            context: Context,
            meta: NavViewPagerContainerMeta
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
                binding.navViewPagerSmartTab.baseDynamicSmartTab.getTabs().forEach { it.findViewById<View>(R.id.tab_text_tv).visibility = View.GONE }
                binding.navViewPagerSmartTab.baseDynamicSmartTab.getTabAt(position).findViewById<View>(R.id.tab_text_tv).visibility = View.VISIBLE
            }
        }
    }

    private lateinit var viewPagerParcelableFragments: NavViewPagerParcelableFragments
    private lateinit var viewPagerMeta: NavViewPagerContainerMeta

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NavViewPagerContainerActivityLayoutBinding {
        return NavViewPagerContainerActivityLayoutBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.navViewPagerToolbar.dynamicToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.navViewPagerSmartTab.baseDynamicSmartTab.setBackgroundColor(DynamicTheme.getInstance().get().primaryColor)

        viewPagerMeta = intent.getParcelableExtra(NavViewPagerContainerMetaKey) ?: return
        prepareViews(viewPagerMeta)

        binding.navViewPager.addOnPageChangeListener(pageChangeListener)
        binding.navViewPager.adapter = ViewPagerContainerAdapter()
        binding.navViewPager.offscreenPageLimit = viewPagerParcelableFragments.clzzes.size - 1
        binding.navViewPagerSmartTab.baseDynamicSmartTab.setViewPager(binding.navViewPager, null)
        binding.navViewPager.setCurrentItem(0, false)
        binding.navViewPager.post {
            pageChangeListener.onPageSelected(binding.navViewPager.currentItem)
        }

    }


    private fun prepareViews(viewPagerMeta: NavViewPagerContainerMeta) {
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
                binding.navViewPagerSmartTab.baseDynamicSmartTab.setCustomTabView { container, position, _ ->
                    val binding = SmartTabLayoutBinding.inflate(inflater, container, false)
                    when (position) {
                        0 -> {
                            createTabView(binding, R.drawable.ic_fire, R.string.overview)
                        }
                        1 -> {
                            createTabView(binding, R.drawable.ic_genre, R.string.genre)
                        }
                        2 -> {
                            createTabView(binding, R.drawable.ic_tag, R.string.tags)
                        }
                        3 -> {
                            createTabView(binding, R.drawable.ic_voice_role, R.string.voice_actors)
                        }
                        4 -> {
                            createTabView(binding, R.drawable.ic_studio, R.string.studios)
                        }
                        5 -> {
                            createTabView(binding, R.drawable.ic_staff, R.string.staff)
                        }
                    }

                    binding.root
                }
            }
            NavViewPagerContainerType.MANGA_STATS -> {
                val inflater = LayoutInflater.from(this)
                binding.navViewPagerSmartTab.baseDynamicSmartTab.setCustomTabView { container, position, _ ->
                    val binding = SmartTabLayoutBinding.inflate(inflater, container, false)
                    when (position) {
                        0 -> {
                            createTabView(binding, R.drawable.ic_fire, R.string.overview)
                        }
                        1 -> {
                            createTabView(binding, R.drawable.ic_genre, R.string.genre)
                        }
                        2 -> {
                            createTabView(binding, R.drawable.ic_tag, R.string.tags)
                        }
                        3 -> {
                            createTabView(binding, R.drawable.ic_staff, R.string.staff)
                        }
                    }
                    binding.root
                }
            }
        }
    }

    private fun createTabView(view:SmartTabLayoutBinding, @DrawableRes src: Int, @StringRes str: Int){
        view.tabImageView.imageTintList = tabColorStateList
        view.tabImageView.setImageResource(src)
        view.tabTextTv.text = getString(str)
        view.root.background = RippleDrawable(ColorStateList.valueOf(tintAccentColor), null, null)
        view.tabTextTv.setTextColor(accentColor)
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