package com.revolgenx.anilib.dialog

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.event.MediaListFilterEvent
import com.revolgenx.anilib.event.meta.MediaListFilterMeta
import com.revolgenx.anilib.field.TagField
import kotlinx.android.synthetic.main.list_filter_dialog_layout.*


class MediaListFilterDialog : DynamicDialogFragment() {
    companion object {
        fun newInstance() = MediaListFilterDialog()
        private const val LIST_FORMAT_KEY = "LIST_FORMAT_KEY"
        private const val LIST_STATUS_KEY = "LIST_STATUS_KEY"
        private const val LIST_GENRE_KEY = "LIST_GENRE_KEY"
    }

    private var alertDialog: DynamicDialog? = null

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        retainInstance = true
        with(dialogBuilder) {
            setTitle(R.string.filter)
            setPositiveButton(R.string.done) { dialogInterface, _ ->
                if (dialogInterface is DynamicDialog) {
                    MediaListFilterMeta(
                        format = (dialogInterface.listFormatSpinner.selectedItemPosition - 1).takeIf { it >= 0 },
                        status = (dialogInterface.listStatusSpinner.selectedItemPosition - 1).takeIf { it >= 0 },
                        genres = (dialogInterface.listGenreSpinner.selectedItemPosition - 1).takeIf { it >= 0 }
                    ).let {
                        MediaListFilterEvent(it).postEvent
                    }
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

    override fun onSaveInstanceState(outState: Bundle) {
        alertDialog?.let {
            outState.putInt(LIST_FORMAT_KEY, it.listFormatSpinner.selectedItemPosition)
            outState.putInt(LIST_STATUS_KEY, it.listStatusSpinner.selectedItemPosition)
            outState.putInt(LIST_GENRE_KEY, it.listGenreSpinner.selectedItemPosition)
        }
        super.onSaveInstanceState(outState)
    }


    override fun onCustomiseDialog(
        dialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        alertDialog = dialog
        dialog.apply {
            setOnShowListener {
                this.updateTheme()
                this.updateView(savedInstanceState)
            }
        }
        return super.onCustomiseDialog(dialog, savedInstanceState)
    }


    private fun DynamicDialog.updateTheme() {
        ThemeController.lightSurfaceColor().let {
            listFormatFrameLayout.setBackgroundColor(it)
            listStatusFrameLayout.setBackgroundColor(it)
            listGenreFrameLayout.setBackgroundColor(it)
        }
    }

    private fun DynamicDialog.updateView(savedInstanceState: Bundle?) {
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

        val listGenreItems = mutableListOf<DynamicSpinnerItem>().apply {
            add(DynamicSpinnerItem(null, getString(R.string.none)))
            addAll(
                context.resources.getStringArray(R.array.media_genre).map {
                    DynamicSpinnerItem(null, it)
                })
        }

        listFormatSpinner.adapter = makeSpinnerAdapter(listFormatItems)
        listStatusSpinner.adapter = makeSpinnerAdapter(listStatusItems)
        listGenreSpinner.adapter = makeSpinnerAdapter(listGenreItems)

        savedInstanceState?.let {
            listFormatSpinner.setSelection(it.getInt(LIST_FORMAT_KEY))
            listStatusSpinner.setSelection(it.getInt(LIST_STATUS_KEY))
            listGenreSpinner.setSelection(it.getInt(LIST_GENRE_KEY))
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