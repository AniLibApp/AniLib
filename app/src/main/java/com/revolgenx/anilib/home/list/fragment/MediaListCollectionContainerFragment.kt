package com.revolgenx.anilib.home.list.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.bottomsheet.BottomSheetFragment
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.AlListContainerFragmentBinding
import com.revolgenx.anilib.list.fragment.AnimeListCollectionFragment
import com.revolgenx.anilib.list.fragment.MediaListCollectionFragment
import com.revolgenx.anilib.list.fragment.MangaListCollectionFragment
import com.revolgenx.anilib.home.list.viewmodel.AlMediaListCollectionSharedVM
import com.revolgenx.anilib.list.bottomsheet.MediaListGroupSelectorBottomSheet
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MediaListCollectionContainerFragment : BaseLayoutFragment<AlListContainerFragmentBinding>() {
    private val fragments: List<MediaListCollectionFragment> by lazy {
        listOf(
            AnimeListCollectionFragment(),
            MangaListCollectionFragment()
        )
    }
    private val sharedViewModel by sharedViewModel<AlMediaListCollectionSharedVM>()

    private val animeListEntries by lazy {
        requireContext().resources.getStringArray(R.array.al_anime_list_status_entries)
    }
    private val mangaListEntries by lazy {
        requireContext().resources.getStringArray(R.array.al_manga_list_status_entries)
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AlListContainerFragmentBinding {
        return AlListContainerFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            alListViewPager.adapter = makeViewPagerAdapter2(fragments)
            TabLayoutMediator(listTabLayout, alListViewPager) { tab, position ->
                tab.text = resources.getStringArray(R.array.list_tab_menu)[position]
            }.attach()

            listSearchIv.setOnClickListener {
                sharedViewModel.toggleSearchView.value = alListViewPager.currentItem
            }

            listExtendedFab.setOnClickListener {
                sharedViewModel.showListGroupSelector.value = alListViewPager.currentItem
            }
        }
    }

}