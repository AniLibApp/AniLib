package com.revolgenx.anilib.home.list.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.MediaListCollectionContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.OpenNotificationCenterEvent
import com.revolgenx.anilib.list.fragment.AnimeListCollectionFragment
import com.revolgenx.anilib.list.fragment.BaseMediaListCollectionFragment
import com.revolgenx.anilib.list.fragment.MangaListCollectionFragment
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionContainerCallback
import com.revolgenx.anilib.list.viewmodel.MediaListContainerSharedVM
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.view.setBoundsFor
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListCollectionContainerFragment :
    BaseLayoutFragment<MediaListCollectionContainerFragmentBinding>() {
    private val fragments: List<BaseMediaListCollectionFragment> by lazy {
        listOf(
            AnimeListCollectionFragment(),
            MangaListCollectionFragment()
        )
    }
    private val sharedViewModel by viewModel<MediaListContainerSharedVM>()
    private val notificationStoreVM by sharedViewModel<NotificationStoreViewModel>()

    private var tabLayoutMediator: TabLayoutMediator? = null
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaListCollectionContainerFragmentBinding {
        return MediaListCollectionContainerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            alListViewPager.adapter = makeViewPagerAdapter2(fragments)
            tabLayoutMediator = TabLayoutMediator(listTabLayout, alListViewPager) { tab, position ->
                tab.text = resources.getStringArray(R.array.list_tab_menu)[position]
            }.also { it.attach() }

            alListViewPager.registerOnPageChangeCallback(object :
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

            listSearchIv.setOnClickListener {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.SEARCH to alListViewPager.currentItem
            }

            listExtendedFab.setOnClickListener {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.GROUP to alListViewPager.currentItem
            }


            initNotificationBadge()

            listNotificationIv.setOnClickListener {
                notificationStoreVM.setUnreadNotificationCount(0)
                OpenNotificationCenterEvent().postEvent
            }

            listMoreIv.onPopupMenuClickListener = { _, position ->
                when (position) {
                    0 -> {
                        sharedViewModel.mediaListContainerCallback.value =
                            MediaListCollectionContainerCallback.DISPLAY to alListViewPager.currentItem
                    }
                    1 -> {
                        sharedViewModel.mediaListContainerCallback.value =
                            MediaListCollectionContainerCallback.FILTER to alListViewPager.currentItem
                    }
                }
            }

        }
    }


    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun initNotificationBadge() {
        binding.listNotificationIv.post {
            val badgeDrawable = BadgeDrawable.create(requireContext())

            badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
            badgeDrawable.backgroundColor = dynamicAccentColor
            badgeDrawable.verticalOffset = 4
            badgeDrawable.horizontalOffset = 4
            badgeDrawable.setBoundsFor(binding.listNotificationIv, binding.listNotifLayout)
            notificationStoreVM.unreadNotificationCount.observe(viewLifecycleOwner, {
                if (it > 0) {
                    BadgeUtils.attachBadgeDrawable(
                        badgeDrawable,
                        binding.listNotificationIv,
                        binding.listNotifLayout
                    )
                } else {
                    BadgeUtils.detachBadgeDrawable(
                        badgeDrawable,
                        binding.listNotificationIv
                    )
                }
            })
        }
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        super.onDestroyView()
    }

}