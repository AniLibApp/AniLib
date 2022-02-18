package com.revolgenx.anilib.airing.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getAiringField
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.storeAiringField
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.constant.ALAiringSort
import com.revolgenx.anilib.databinding.AiringFilterDialogLayoutBinding
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.util.loginContinue

class AiringFilterBottomSheet : DynamicBottomSheetFragment<AiringFilterDialogLayoutBinding>() {
    companion object {
        fun newInstance(): AiringFilterBottomSheet {
            return AiringFilterBottomSheet()
        }
    }

    private val airingField by lazy {
        getAiringField(requireContext())
    }

    var onDoneListener: (() -> Unit)? = null

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): AiringFilterDialogLayoutBinding {
        return AiringFilterDialogLayoutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            showAllAiringSwitch.isChecked = !airingField.notYetAired

            loginContinue(false) {
                showFromWatchListSwitch.visibility = View.VISIBLE
                showFromPlanningListSwitch.visibility = View.VISIBLE
                showFromPlanningListSwitch.isChecked = airingField.showFromPlanning
                showFromWatchListSwitch.isChecked = airingField.showFromWatching
            }


            val saveSortIndex: Int
            val savedSortOrder: SortOrder
            val alAiringSortEnums = ALAiringSort.values()


            val savedAiringSort = airingField.sort!!

            savedSortOrder = if (savedAiringSort % 2 == 0) {
                saveSortIndex = alAiringSortEnums.first { it.sort == savedAiringSort }.ordinal
                SortOrder.ASC
            } else {
                saveSortIndex = alAiringSortEnums.first { it.sort == savedAiringSort - 1 }.ordinal
                SortOrder.DESC
            }

            resources.getStringArray(R.array.al_airing_sort).mapIndexed { index, s ->
                AniLibSortingModel(
                    alAiringSortEnums[index],
                    s,
                    if (index == saveSortIndex) savedSortOrder else SortOrder.NONE,
                    allowNone = false
                )
            }.let {
                alAiringSort.setSortItems(it)
            }

            initListener()
        }
    }

    private fun AiringFilterDialogLayoutBinding.initListener() {
        onPositiveClicked = {
            airingField.notYetAired = !showAllAiringSwitch.isChecked
            airingField.showFromPlanning = showFromPlanningListSwitch.isChecked
            airingField.showFromWatching = showFromWatchListSwitch.isChecked
            airingField.sort = getActiveAiringSort()
            storeAiringField(requireContext(), airingField)
            onDoneListener?.invoke()
        }
    }

    private fun getActiveAiringSort(): Int {
        return binding.alAiringSort.getActiveSortItem()!!.let {
            if (it.order == SortOrder.DESC) {
                (it.data as ALAiringSort).sort + 1
            } else {
                (it.data as ALAiringSort).sort
            }
        }
    }

}