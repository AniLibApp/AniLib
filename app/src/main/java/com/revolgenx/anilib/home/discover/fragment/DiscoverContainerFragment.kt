package com.revolgenx.anilib.home.discover.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.registerOnPageChangeCallback
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.home.season.data.field.SeasonField
import com.revolgenx.anilib.databinding.DiscoverContainerFragmentBinding
import com.revolgenx.anilib.common.event.*
import com.revolgenx.anilib.home.discover.bottomsheet.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.home.discover.event.SeasonEvent
import com.revolgenx.anilib.home.recommendation.event.RecommendationEvent
import com.revolgenx.anilib.home.season.dialog.ShowSeasonHeaderDialog
import com.revolgenx.anilib.home.recommendation.fragment.RecommendationFragment
import com.revolgenx.anilib.home.season.fragment.SeasonFragment
import com.revolgenx.anilib.home.recommendation.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.home.season.viewmodel.SeasonViewModel
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverContainerFragment : BaseLayoutFragment<DiscoverContainerFragmentBinding>() {

    private val discoverFragments by lazy {
        listOf(
            DiscoverFragment(),
            SeasonFragment(),
            RecommendationFragment()
        )
    }

    private val seasonViewModel by viewModel<SeasonViewModel>()
    private val recommendationViewModel by viewModel<RecommendationViewModel>()
    private val notificationStoreVM by sharedViewModel<NotificationStoreViewModel>()

    override val menuRes: Int = R.menu.discover_container_fragment_menu

    private val seasons by lazy {
        requireContext().resources.getStringArray(R.array.media_season)
    }

    private val badgeDrawable by lazy {
        BadgeDrawable.create(requireContext())
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DiscoverContainerFragmentBinding {
        return DiscoverContainerFragmentBinding.inflate(inflater, parent, false)
    }


    @SuppressLint("UnsafeExperimentalUsageError")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.initListener()
        binding.initSeasonListener()
        binding.initRecommendationListener()

        binding.discoverContainerViewPager.adapter = makeViewPagerAdapter2(
            discoverFragments,
        )
        setupWithViewPager2(
            binding.discoverMainTabLayout,
            binding.discoverContainerViewPager,
            resources.getStringArray(R.array.discover_tab_menu)
        )

        binding.discoverContainerViewPager.offscreenPageLimit = 3

        if (savedInstanceState == null) {
            seasonViewModel.field = SeasonField.create()

            with(recommendationViewModel.field) {
                sort = getRecommendationSort()
                onList = getRecommendationOnList()
            }
        }
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onToolbarInflated() {
        val notificationMenuItem = getBaseToolbar().menu.findItem(R.id.discover_notification_menu)
        if (requireContext().loggedIn()) {
            notificationStoreVM.unreadNotificationCount.observe(viewLifecycleOwner) {
                if (it > 0) {
                    BadgeUtils.attachBadgeDrawable(
                        badgeDrawable,
                        getBaseToolbar(),
                        R.id.discover_notification_menu
                    )
                } else {
                    BadgeUtils.detachBadgeDrawable(
                        badgeDrawable,
                        getBaseToolbar(),
                        R.id.discover_notification_menu
                    )
                }
            }
        } else {
            notificationMenuItem.isVisible = false
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.discover_search_menu -> {
                OpenSearchEvent().postEvent
                true
            }
            R.id.discover_notification_menu -> {
                notificationStoreVM.setUnreadNotificationCount(0)
                OpenNotificationCenterEvent().postEvent
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }

    }


    private fun DiscoverContainerFragmentBinding.initSeasonListener() {
        seasonFilterFooter.setOnClickListener {

        }
        seasonPreviousIv.setOnClickListener {
            seasonViewModel.previousSeason(requireContext())
            updateSeasonFooterTitle()
            SeasonEvent.SeasonChangeEvent.postEvent
        }

        seasonNextIv.setOnClickListener {
            seasonViewModel.nextSeason()
            updateSeasonFooterTitle()
            SeasonEvent.SeasonChangeEvent.postEvent
        }

        seasonFilterIv.onPopupMenuClickListener = { _, position ->
            when (position) {
                0 -> {
                    MediaFilterBottomSheetFragment().show(requireContext()) {
                        arguments =
                            bundleOf(MediaFilterBottomSheetFragment.mediaFilterTypeKey to MediaFilterBottomSheetFragment.MediaFilterType.SEASON.ordinal)
                        onDoneListener = {
                            seasonViewModel.field.updateFields()
                            updateSeasonFooterTitle()
                            SeasonEvent.SeasonFilterEvent.postEvent
                        }
                    }
                }
                1 -> {
                    SeasonEvent.SeasonGenreEvent.postEvent
                }
                2 -> {
                    SeasonEvent.SeasonTagEvent.postEvent
                }
                3 -> {
                    ShowSeasonHeaderDialog().also {
                        it.onDoneListener = onDone@{ isChecked, isChanged ->
                            if (context == null) return@onDone
                            if (isChanged) {
                                SeasonEvent.SeasonHeaderEvent(isChecked).postEvent
                            }
                        }
                    }.show(requireContext())
                }
            }
        }
    }

    private fun DiscoverContainerFragmentBinding.initRecommendationListener() {
        if (requireContext().loggedIn()) {
            recommendationOnListCheckBox.visibility = View.VISIBLE
            recommendationOnListCheckBox.setOnCheckedChangeListener(null)
            recommendationOnListCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val onList =
                    if (requireContext().loggedIn()) isChecked else null
                recommendationViewModel.field.onList = onList
                RecommendationEvent.RecommendationFilterEvent(
                    onList,
                    recommendationViewModel.field.sort ?: 0
                ).postEvent
            }
        } else {
            recommendationOnListCheckBox.visibility = View.GONE
        }


        recommendationSortSpinner.onItemSelectedListener = null
        recommendationSortSpinner.setSelection(recommendationViewModel.field.sort ?: 0, false)
        recommendationSortSpinner.onItemSelected { position ->
            RecommendationEvent.RecommendationFilterEvent(
                recommendationViewModel.field.onList,
                position
            ).postEvent
        }
    }


    private fun DiscoverContainerFragmentBinding.initListener() {
        registerOnPageChangeCallback(
            discoverContainerViewPager, object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                    when (position) {
                        1 -> {
                            updateSeasonFooterTitle()
                            recommendationFilterFooter.visibility = View.GONE
                            seasonFilterFooter.visibility = View.VISIBLE
                            val behavior =
                                (seasonFilterFooter.layoutParams as CoordinatorLayout.LayoutParams).behavior
                            (behavior as HideBottomViewOnScrollBehavior).slideUp(seasonFilterFooter)
                            discoverContainerAppBar.setExpanded(true, true)
                        }
                        2 -> {
                            seasonFilterFooter.visibility = View.GONE
                            recommendationFilterFooter.visibility = View.VISIBLE
                            val behavior =
                                (recommendationFilterFooter.layoutParams as CoordinatorLayout.LayoutParams).behavior
                            (behavior as HideBottomViewOnScrollBehavior).slideUp(
                                recommendationFilterFooter
                            )
                            discoverContainerAppBar.setExpanded(true, true)
                        }
                        else -> {
                            recommendationFilterFooter.visibility = View.GONE
                            seasonFilterFooter.visibility = View.GONE
                        }
                    }

                }
            })

    }


    private fun DiscoverContainerFragmentBinding.updateSeasonFooterTitle() {
        seasonViewModel.field.let { field ->
            var toolbarTitle = ""
            field.season?.let { season ->
                toolbarTitle += seasons[season]
            }
            field.seasonYear?.let {
                toolbarTitle += " $it"
            }
            seasonNameTv.text = toolbarTitle
        }
    }
}