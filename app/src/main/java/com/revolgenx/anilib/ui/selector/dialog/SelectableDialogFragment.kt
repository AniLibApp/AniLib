package com.revolgenx.anilib.ui.selector.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.SelectableDialogFragmentBinding
import com.revolgenx.anilib.ui.selector.data.meta.SelectableMeta
import com.revolgenx.anilib.ui.selector.constant.SelectedState
import com.revolgenx.anilib.ui.selector.presenter.SelectableItemPresenter

class SelectableDialogFragment : BaseDialogFragment<SelectableDialogFragmentBinding>() {
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    companion object {
        private const val selectable_meta_key = "selectable_meta_key"
        fun newInstance(
            selectableMeta: SelectableMeta
        ) = SelectableDialogFragment().apply {
            arguments = bundleOf(selectable_meta_key to selectableMeta)
        }
    }


    private val selectableMeta by lazy {
        arguments?.getParcelable<SelectableMeta?>(selectable_meta_key)
    }
    override val titleRes: Int get() = selectableMeta?.title ?: R.string.select
    private val hasIntermediaMode get() = selectableMeta?.hasIntermediateMode ?: false
    private val selectableItems get() = selectableMeta?.selectableItems ?: emptyList()


    private val selectorPresenter by lazy {
        SelectableItemPresenter(requireContext(), hasIntermediaMode)
    }

    override fun bindView(): SelectableDialogFragmentBinding {
        return SelectableDialogFragmentBinding.inflate(provideLayoutInflater)
    }

    override fun builder(dialogBuilder: DynamicDialog.Builder, savedInstanceState: Bundle?) {
        selectableMeta ?: return
        invalidateAdapter()
    }

    private fun invalidateAdapter() {
        Adapter.builder(this)
            .addPresenter(selectorPresenter)
            .addSource(Source.fromList(selectableItems))
            .into(binding.selectableRecyclerView)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        selectableMeta ?: return
        with(alertDialog) {
            getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                selectableItems.forEach { it.second = SelectedState.NONE }
                invalidateAdapter()
            }
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
    }

}