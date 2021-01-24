package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getDiscoverAiringField
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.storeDiscoverAiringField
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.AiringFilterDialogLayoutBinding
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter

class DiscoverAiringFilterDialog : BaseDialogFragment<AiringFilterDialogLayoutBinding>() {

    private val airingField by lazy {
        getDiscoverAiringField(requireContext())
    }

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel
    override var titleRes: Int? = R.string.filter


    override fun bindView(): AiringFilterDialogLayoutBinding {
        return AiringFilterDialogLayoutBinding.inflate(provideLayoutInflater())
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        binding.showAllAiringSwitch.isChecked = !airingField.notYetAired

        if (requireContext().loggedIn()) {
            binding.showFromWatchListSwitch.visibility = View.VISIBLE
            binding.showFromPlanningListSwitch.visibility = View.VISIBLE
            binding.showFromPlanningListSwitch.isChecked = airingField.showFromPlanning
            binding.showFromWatchListSwitch.isChecked = airingField.showFromWatching
        }

        binding.airingSortSpinner.adapter =
            makeSpinnerAdapter(requireContext(), resources.getStringArray(R.array.airing_sort).map {
                DynamicSpinnerItem(
                    null, it
                )
            })

        binding.airingSortSpinner.setSelection(airingField.sort!!)
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        airingField.notYetAired = !binding.showAllAiringSwitch.isChecked
        airingField.showFromPlanning = binding.showFromPlanningListSwitch.isChecked
        airingField.showFromWatching = binding.showFromWatchListSwitch.isChecked

        airingField.sort = binding.airingSortSpinner.selectedItemPosition
        storeDiscoverAiringField(requireContext(), airingField)
        super.onPositiveClicked(dialogInterface, which)
    }

}