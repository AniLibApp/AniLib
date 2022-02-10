package com.revolgenx.anilib.home.discover.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getDiscoverMediaListSort
import com.revolgenx.anilib.common.preference.setDiscoverMediaListSort
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.constant.ALMediaListSort
import com.revolgenx.anilib.databinding.MediaListFilterDialogLayoutBinding
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder

class DiscoverMediaListFilterDialog :
    DynamicBottomSheetFragment<MediaListFilterDialogLayoutBinding>() {
    var onDoneListener: (() -> Unit)? = null

    private val alMediaListSorts by lazy {
        requireContext().resources.getStringArray(R.array.al_media_list_sort)
    }

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MediaListFilterDialogLayoutBinding {
        return MediaListFilterDialogLayoutBinding.inflate(inflater, container, false)
    }

    companion object {
        private const val MEDIA_LIST_FILTER_TYPE = "MEDIA_LIST_FILTER_TYPE"
        private const val MEDIA_LIST_SORT_KEY = "MEDIA_LIST_SORT_KEY"

        fun newInstance(type: Int): DiscoverMediaListFilterDialog {
            return DiscoverMediaListFilterDialog().apply {
                arguments = bundleOf(MEDIA_LIST_FILTER_TYPE to type)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val type = arguments?.getInt(MEDIA_LIST_FILTER_TYPE)!!
            val savedMediaSort =
                savedInstanceState?.getInt(MEDIA_LIST_SORT_KEY) ?: getDiscoverMediaListSort(
                    requireContext(),
                    type
                )

            var savedSortOrder: SortOrder = SortOrder.NONE
            val alMediaListSortEnums = ALMediaListSort.values()
            var savedSortIndex = -1

            savedMediaSort?.takeIf { it > -1 }?.let { savedSort ->
                savedSortOrder = if (savedSort % 2 == 0) {
                    savedSortIndex =
                        alMediaListSortEnums.first { it.sort == savedSort }.ordinal
                    SortOrder.ASC
                } else {
                    savedSortIndex =
                        alMediaListSortEnums.first { it.sort == savedSort - 1 }.ordinal
                    SortOrder.DESC
                }
            }

            val alMediaListSortModels = alMediaListSorts.mapIndexed { index, sort ->
                AniLibSortingModel(
                    alMediaListSortEnums[index],
                    sort,
                    if (savedSortIndex == index) savedSortOrder else SortOrder.NONE
                )
            }

            mediaListSort.setSortItems(alMediaListSortModels)

            initListener()
        }
    }

    private fun initListener() {
        onPositiveClicked = {
            setDiscoverMediaListSort(
                requireContext(),
                arguments?.getInt(MEDIA_LIST_FILTER_TYPE)!!,
                getActiveListSort()
            )
            onDoneListener?.invoke()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            MEDIA_LIST_SORT_KEY,
            getActiveListSort() ?: -1
        )
    }


    private fun getActiveListSort(): Int? {
        return binding.mediaListSort.getActiveSortItem()?.let {
            var activeSort = (it.data as ALMediaListSort).sort
            if (it.order == SortOrder.DESC) {
                activeSort += 1
            }
            activeSort
        }
    }

}