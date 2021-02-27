package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.FormatSelectionDialogBinding

class FormatSelectionDialog : BaseDialogFragment<FormatSelectionDialogBinding>() {

    override var titleRes: Int? = R.string.format
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    private val formatLists get() = requireContext().resources.getStringArray(R.array.media_format)

    private val selectableFormats = mutableListOf<SelectableFormat>()

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    var onDoneListener: ((format: List<Int>) -> Unit)? = null

    companion object {

        private const val FORMAT_SELECTION_DATA_KEY = "FORMAT_SELECTION_DATA_KEY"

        fun newInstance(formats: List<Int>): FormatSelectionDialog {
            return FormatSelectionDialog().also {
                it.arguments = bundleOf(FORMAT_SELECTION_DATA_KEY to formats.toIntArray())
            }
        }
    }

    override fun bindView(): FormatSelectionDialogBinding {
        return FormatSelectionDialogBinding.inflate(provideLayoutInflater)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)

        val data = arguments?.getIntArray(FORMAT_SELECTION_DATA_KEY) ?: return

        if (savedInstanceState == null) {
            formatLists.forEachIndexed { index, s ->
                selectableFormats.add(SelectableFormat(data.contains(index), s, index))
            }
        }

        binding.formatSelectionRecyclerView.adapter = Adapter.builder(this)
            .addSource(Source.fromList(selectableFormats))
            .addPresenter(
                Presenter.simple<SelectableFormat>(
                    requireContext(),
                    R.layout.checkable_presenter,
                    0
                ) { v, format ->
                    val checkbox = v.findViewById<DynamicCheckBox>(R.id.checkable_checkbox)
                    checkbox.setTextColor(dynamicTheme.textPrimaryColor)
                    checkbox.isChecked = format.isChecked
                    checkbox.text = format.format
                    checkbox.setOnCheckedChangeListener { _, isChecked ->
                        format.isChecked = isChecked
                    }

                })
            .build()

    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        super.onPositiveClicked(dialogInterface, which)
        onDoneListener?.invoke(selectableFormats.filter { it.isChecked }.map { it.position })
    }

    data class SelectableFormat(
        var isChecked: Boolean = false,
        var format: String,
        var position: Int
    )

}