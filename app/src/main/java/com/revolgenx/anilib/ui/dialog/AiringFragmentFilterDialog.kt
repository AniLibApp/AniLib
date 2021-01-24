package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getAiringField
import com.revolgenx.anilib.common.preference.getDiscoverAiringField
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.data.meta.AiringFilterMeta
import com.revolgenx.anilib.databinding.AiringFilterDialogLayoutBinding
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter

class AiringFragmentFilterDialog : BaseDialogFragment<AiringFilterDialogLayoutBinding>() {
    companion object {
        fun newInstance(): AiringFragmentFilterDialog {
            return AiringFragmentFilterDialog()
        }
    }

    private val airingMeta by lazy {
        getAiringField(requireContext()).let {
            AiringFilterMeta(
                it.notYetAired,
                it.showFromWatching,
                it.showFromPlanning,
                it.sort
            )
        }
    }

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel
    override var titleRes: Int? = R.string.filter

    var onDoneListener: ((meta: AiringFilterMeta) -> Unit)? = null

    override fun bindView(): AiringFilterDialogLayoutBinding {
        return AiringFilterDialogLayoutBinding.inflate(provideLayoutInflater())
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        binding.showAllAiringSwitch.isChecked = !airingMeta.notYetAired

        if (requireContext().loggedIn()) {
            binding.showFromWatchListSwitch.visibility = View.VISIBLE
            binding.showFromPlanningListSwitch.visibility = View.VISIBLE
            binding.showFromPlanningListSwitch.isChecked = airingMeta.showFromPlanning
            binding.showFromWatchListSwitch.isChecked = airingMeta.showFromWatching
        }

        binding.airingSortSpinner.adapter =
            makeSpinnerAdapter(requireContext(), resources.getStringArray(R.array.airing_sort).map {
                DynamicSpinnerItem(
                    null, it
                )
            })

        binding.airingSortSpinner.setSelection(airingMeta.sort!!)
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        airingMeta.notYetAired = !binding.showAllAiringSwitch.isChecked
        airingMeta.showFromPlanning = binding.showFromPlanningListSwitch.isChecked
        airingMeta.showFromWatching = binding.showFromWatchListSwitch.isChecked
        airingMeta.sort = binding.airingSortSpinner.selectedItemPosition

        onDoneListener?.invoke(airingMeta)

        super.onPositiveClicked(dialogInterface, which)
    }

}