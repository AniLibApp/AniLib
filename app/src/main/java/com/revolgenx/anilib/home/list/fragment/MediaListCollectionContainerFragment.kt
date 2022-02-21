package com.revolgenx.anilib.home.list.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.viewmodel.MainSharedVM
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.registerOnPageChangeCallback
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.MediaListCollectionContainerFragmentBinding
import com.revolgenx.anilib.common.event.OpenNotificationCenterEvent
import com.revolgenx.anilib.list.fragment.AnimeListCollectionFragment
import com.revolgenx.anilib.list.fragment.BaseMediaListCollectionFragment
import com.revolgenx.anilib.list.fragment.MangaListCollectionFragment
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionContainerCallback
import com.revolgenx.anilib.list.viewmodel.MediaListContainerSharedVM
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.type.MediaType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListCollectionContainerFragment :
    BaseLayoutFragment<MediaListCollectionContainerFragmentBinding>() {
    override val menuRes: Int = R.menu.media_list_collection_container_menu

    private val fragments: List<BaseMediaListCollectionFragment> by lazy {
        listOf(
            AnimeListCollectionFragment(),
            MangaListCollectionFragment()
        )
    }
    private val sharedViewModel by viewModel<MediaListContainerSharedVM>()
    private val mainSharedVM by sharedViewModel<MainSharedVM>()
    private val notificationStoreVM by sharedViewModel<NotificationStoreViewModel>()

    private val badgeDrawable by lazy {
        BadgeDrawable.create(requireContext())
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaListCollectionContainerFragmentBinding {
        return MediaListCollectionContainerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun onToolbarInflated() {
        val notificationMenuItem = getBaseToolbar().menu.findItem(R.id.list_notification_menu)
        if (requireContext().loggedIn()) {
            notificationStoreVM.unreadNotificationCount.observe(viewLifecycleOwner) {
                if (it > 0) {
                    BadgeUtils.attachBadgeDrawable(
                        badgeDrawable,
                        getBaseToolbar(),
                        R.id.list_notification_menu
                    )
                } else {
                    BadgeUtils.detachBadgeDrawable(
                        badgeDrawable,
                        getBaseToolbar(),
                        R.id.list_notification_menu
                    )
                }
            }
        } else {
            notificationMenuItem.isVisible = false
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_search_menu -> {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.SEARCH to binding.alListViewPager.currentItem
                true
            }
            R.id.list_notification_menu -> {
                notificationStoreVM.setUnreadNotificationCount(0)
                OpenNotificationCenterEvent().postEvent
                true
            }
            R.id.list_display_mode_menu -> {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.DISPLAY to binding.alListViewPager.currentItem
                true
            }
            R.id.list_filter_menu -> {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.FILTER to binding.alListViewPager.currentItem
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            alListViewPager.adapter = makeViewPagerAdapter2(fragments)
            setupWithViewPager2(
                listTabLayout,
                alListViewPager,
                resources.getStringArray(R.array.list_tab_menu)
            )
            registerOnPageChangeCallback(alListViewPager, object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sharedViewModel.mediaListContainerCallback.value =
                        MediaListCollectionContainerCallback.CURRENT_TAB to if (position == 0)
                            MediaType.ANIME.ordinal
                        else MediaType.MANGA.ordinal
                }
            })

            sharedViewModel.currentGroupNameWithCount.observe(viewLifecycleOwner) {
                if (it == null) {
                    listExtendedFab.text =
                        "%s %d".format(requireContext().getString(R.string.all), 0)
                } else {
                    listExtendedFab.text = "%s %d".format(it.first, it.second)
                }
            }

            listExtendedFab.setOnClickListener {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.GROUP to alListViewPager.currentItem
            }

            mainSharedVM.mediaListCurrentTab.observe(viewLifecycleOwner) {
                when (it) {
                    MediaType.ANIME.ordinal -> {
                        alListViewPager.post {
                            alListViewPager.currentItem = 0
                        }
                    }
                    MediaType.MANGA.ordinal -> {
                        alListViewPager.post {
                            alListViewPager.currentItem = 1
                        }
                    }
                    else -> {
                        return@observe
                    }
                }
                mainSharedVM.mediaListCurrentTab.value = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}