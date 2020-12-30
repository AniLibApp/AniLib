package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getAiringField
import com.revolgenx.anilib.common.preference.storeAiringField
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.AiringFilterDialogLayoutBinding

class AiringFilterDialog : BaseDialogFragment() {

    private val airingField by lazy {
        getAiringField(requireContext())
    }

    override var viewRes: Int? = R.layout.airing_filter_dialog_layout
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    private lateinit var binding:AiringFilterDialogLayoutBinding


    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        binding = AiringFilterDialogLayoutBinding.bind(dialogView)
        binding.showAllAiringSwitch.isChecked = !airingField.notYetAired

        binding.airingSortSpinner.adapter = makeSpinnerAdapter(resources.getStringArray(R.array.airing_sort).map { DynamicSpinnerItem(
            null, it
        ) })

        binding.airingSortSpinner.setSelection(airingField.sort!!)
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        super.onPositiveClicked(dialogInterface, which)
        airingField.notYetAired = !binding.showAllAiringSwitch.isChecked
        airingField.sort = binding.airingSortSpinner.selectedItemPosition
        storeAiringField(requireContext(), airingField)
    }


    private fun makeSpinnerAdapter(items: List<DynamicSpinnerItem>) =
        DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )

}