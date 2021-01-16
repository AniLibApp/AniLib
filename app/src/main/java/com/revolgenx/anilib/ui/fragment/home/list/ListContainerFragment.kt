package com.revolgenx.anilib.ui.fragment.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.popup.DynamicArrayPopup
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.setMediaListGridPresenter
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ListContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.BrowseNotificationEvent
import com.revolgenx.anilib.infrastructure.event.DisplayModeChangedEvent
import com.revolgenx.anilib.infrastructure.event.DisplayTypes
import com.revolgenx.anilib.infrastructure.event.MediaListCollectionFilterEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.bottomsheet.list.MediaListFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe

class ListContainerFragment : BaseLayoutFragment<ListContainerFragmentBinding>(), EventBusListener {

    private lateinit var adapter: FragmentPagerAdapter

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ListContainerFragmentBinding {
        return ListContainerFragmentBinding.inflate(inflater, parent, false)
    }

    private val listContainerFragments by lazy {
        listOf(
            AnimeListContainerFragment(),
            MangaListContainerFragment()
        )
    }


    private val onPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.setCurrentStatusFab()
        }
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    @Subscribe()
    fun onEvent(event: MediaListCollectionFilterEvent) {
        binding.getCurrentListFragment().filterList(event.meta)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            adapter = makePagerAdapter(
                listContainerFragments,
                resources.getStringArray(R.array.list_tab_menu)
            )

            listViewPager.adapter = adapter

            initListener()

            listViewPager.post {
                setCurrentStatusFab()
                binding.listExtendedFab.isAllowExtended = true
                binding.listExtendedFab.isExtended = true
            }

        }
    }

    private fun ListContainerFragmentBinding.setCurrentStatusFab() {
        binding.listExtendedFab.text =
            listContainerFragments[listViewPager.currentItem].getStatusName()
    }

    private fun ListContainerFragmentBinding.initListener() {
        listViewPager.addOnPageChangeListener(onPageChangeListener)
        listTabLayout.setupWithViewPager(listViewPager)

        listSearchIv.setOnClickListener {
            getCurrentListFragment().showSearchET()
        }

        listExtendedFab.setOnClickListener {
            DynamicArrayPopup(
                it,
                getCurrentListFragment().getStatus()
            ) { _, _, position, _ ->
                getCurrentListFragment().setCurrentStatus(position)
                setCurrentStatusFab()
            }.let { popup ->
                popup.viewType = DynamicPopup.Type.LIST
                popup.build().show()
            }
        }

        listExtendedFab.setOnLongClickListener {
            MediaListFilterBottomSheetFragment().show(requireContext(), listViewPager.currentItem) {
            }
            true
        }

        listNotificationIv.setOnClickListener {
            BrowseNotificationEvent().postEvent
        }

        listMoreIv.onPopupMenuClickListener = { _, position ->
            when (position) {
                0 -> {
                    makeArrayPopupMenu(
                        listMoreIv,
                        requireContext().resources.getStringArray(R.array.list_display_modes),
                        selectedPosition = getMediaListGridPresenter().ordinal
                    ) { _, _, index, _ ->
                        setMediaListGridPresenter(index)
                        DisplayModeChangedEvent(DisplayTypes.MEDIA_LIST).postEvent
                    }
                }
                1 -> {
                    MediaListFilterBottomSheetFragment().show(
                        requireContext(),
                        listViewPager.currentItem
                    ) {
                    }
                }
            }
        }
    }

    private fun ListContainerFragmentBinding.getCurrentListFragment(): MediaListContainerFragment {
        return listContainerFragments[listViewPager.currentItem]
    }

    fun goToListType(type: Int) {
        binding.listViewPager.setCurrentItem(if (type == MediaType.ANIME.ordinal) 0 else 1, false)
    }

    override fun onDestroyView() {
        binding.listViewPager.clearOnPageChangeListeners()
        super.onDestroyView()
    }
}
