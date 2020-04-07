package com.revolgenx.anilib.dialog

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import kotlinx.android.synthetic.main.list_filter_dialog_layout.*

class ListFilterDialog : DynamicDialogFragment() {


    companion object{
        fun newInstance() = ListFilterDialog()
    }

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        retainInstance = true
        with(dialogBuilder) {
            setTitle(R.string.filter)
            setPositiveButton(R.string.done) { dialogInterface, _ ->
                if (dialogInterface is DynamicDialog) {

                }
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }
            setView(R.layout.list_filter_dialog_layout)
            isAutoDismiss = false
        }

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }


    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        alertDialog.apply {
            setOnShowListener {
                this.updateNavTheme()
                this.updateNavView()
            }
        }
        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }


    private fun DynamicDialog.updateNavTheme() {
        ThemeController.lightSurfaceColor().let {
            listFormatFrameLayout.setBackgroundColor(it)
            listStatusFrameLayout.setBackgroundColor(it)
            listGenreFrameLayout.setBackgroundColor(it)
        }
    }

    private fun DynamicDialog.updateNavView() {
        val listFormatItems = context.resources.getStringArray(R.array.advance_search_format).map {
            DynamicSpinnerItem(
                null, it
            )
        }
        val listStatusItems = context.resources.getStringArray(R.array.advance_search_status).map {
            DynamicSpinnerItem(
                null, it
            )
        }

        listFormatSpinner.adapter = makeSpinnerAdapter(listFormatItems)
        listStatusSpinner.adapter = makeSpinnerAdapter(listStatusItems)
    }


    private fun makeSpinnerAdapter(items: List<DynamicSpinnerItem>) =
        DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )

}