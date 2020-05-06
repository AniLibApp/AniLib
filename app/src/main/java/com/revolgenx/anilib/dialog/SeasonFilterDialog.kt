package com.revolgenx.anilib.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R
import com.revolgenx.anilib.preference.getSeasonField
import kotlinx.android.synthetic.main.season_filter_layout.*

class SeasonFilterDialog : DynamicDialogFragment() {

    var onDoneListener: (() -> Unit)? = null
    private val listFormatItems by lazy {
        requireContext().resources.getStringArray(R.array.advance_search_format).map {
            DynamicSpinnerItem(
                null, it
            )
        }
    }

    private val listStatusItems by lazy {
        requireContext().resources.getStringArray(R.array.advance_search_status).map {
            DynamicSpinnerItem(
                null, it
            )
        }
    }


    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            setTitle(R.string.season_filter)
            setView(R.layout.season_filter_layout)
            setPositiveButton(R.string.done) { dialogInterface, _ ->
                if (dialogInterface is DynamicDialog) {
                    val field = getSeasonField(requireContext())
                    field.format = dialogInterface.seasonFormatSpinner.selectedItemPosition.minus(1)
                        .takeIf { it >= 0 }
                    field.status = dialogInterface.seasonStatusSpinner.selectedItemPosition.minus(1)
                        .takeIf { it >= 0 }
                    field.saveSeasonField(requireContext())
                    onDoneListener?.invoke()
                }
            }

            setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }
            isAutoDismiss = false
        }
        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        with(alertDialog) {
            setOnShowListener {
                getButton(AlertDialog.BUTTON_POSITIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }

                getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }

                seasonStatusSpinner.adapter = makeSpinnerAdapter(listStatusItems)
                seasonFormatSpinner.adapter = makeSpinnerAdapter(listFormatItems)

                val field = getSeasonField(requireContext())
                field.status?.let {
                    seasonStatusSpinner.setSelection(it + 1)
                }
                field.format?.let {
                    seasonFormatSpinner.setSelection(it + 1)
                }
            }
            return super.onCustomiseDialog(alertDialog, savedInstanceState)
        }
    }


    private fun makeSpinnerAdapter(items: List<DynamicSpinnerItem>) =
        DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )

}

