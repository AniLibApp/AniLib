package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.databinding.UserMediaListCollectionContainerFragmentBinding
import com.revolgenx.anilib.list.viewmodel.MediaListContainerSharedVM
import com.revolgenx.anilib.list.fragment.AnimeListCollectionFragment
import com.revolgenx.anilib.list.fragment.MangaListCollectionFragment
import com.revolgenx.anilib.list.fragment.BaseMediaListCollectionFragment
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionContainerCallback
import com.revolgenx.anilib.type.MediaType
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserMediaListCollectionContainerFragment :
    BaseLayoutFragment<UserMediaListCollectionContainerFragmentBinding>() {

    override val menuRes: Int = R.menu.user_media_list_collection_container_menu
    override val setHomeAsUp: Boolean = true

    companion object {
        private const val media_list_meta_key = "media_list_meta_key"
        fun newInstance(meta: MediaListMeta) = UserMediaListCollectionContainerFragment().also {
            it.arguments = bundleOf(media_list_meta_key to meta)
        }
    }

    private val userMediaListMeta
        get() = arguments?.getParcelable<MediaListMeta?>(
            media_list_meta_key
        )

    private val fragments: List<BaseMediaListCollectionFragment> by lazy {
        listOf(
            AnimeListCollectionFragment(),
            MangaListCollectionFragment()
        )
    }
    private val sharedViewModel by viewModel<MediaListContainerSharedVM>()

    private var tabLayoutMediator: TabLayoutMediator? = null
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserMediaListCollectionContainerFragmentBinding {
        return UserMediaListCollectionContainerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_search_menu -> {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.SEARCH to binding.userListViewPager.currentItem
                true
            }
            R.id.list_filter_menu -> {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.FILTER to binding.userListViewPager.currentItem
                true
            }
            R.id.list_display_mode_menu -> {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.DISPLAY to binding.userListViewPager.currentItem
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userMediaListMeta ?: return
        sharedViewModel.userId = userMediaListMeta?.userId ?: return

        binding.apply {
            userListViewPager.adapter = makeViewPagerAdapter2(fragments)
            tabLayoutMediator =
                TabLayoutMediator(listTabLayout, userListViewPager) { tab, position ->
                    tab.text = resources.getStringArray(R.array.list_tab_menu)[position]
                }.also { it.attach() }

            userListViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sharedViewModel.mediaListContainerCallback.value =
                        MediaListCollectionContainerCallback.CURRENT_TAB to
                                if (position == 0)
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

            userMediaListMeta?.type?.let {
                userListViewPager.post {
                    userListViewPager.currentItem = if (it == MediaType.ANIME.ordinal) 0 else 1
                }
            }

            listExtendedFab.setOnClickListener {
                sharedViewModel.mediaListContainerCallback.value =
                    MediaListCollectionContainerCallback.GROUP to userListViewPager.currentItem

            }
        }
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        super.onDestroyView()
    }

}