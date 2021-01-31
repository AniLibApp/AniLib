package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.InputDialogLayoutBinding
import com.revolgenx.anilib.util.getClipBoardText


class InputDialog : BaseDialogFragment<InputDialogLayoutBinding>() {
    companion object {
        const val titleKey = "title_key"
        private const val inputTypeKey = "input_type_key"
        private const val defaultInputKey = "default_input_key"
        private const val showPasteButtonKey = "show_paste_button_key"
        val tag = InputDialog::class.java.simpleName
        fun newInstance(
            title: Int? = null,
            inputType: Int? = null,
            default: String? = null,
            showPasteButton: Boolean = true
        ): InputDialog {
            return InputDialog().also {
                it.arguments = bundleOf(
                    titleKey to title,
                    inputTypeKey to inputType,
                    defaultInputKey to default,
                    showPasteButtonKey to showPasteButton
                )
            }
        }
    }

    var onInputDoneListener: ((str: String) -> Unit)? = null

    override var titleRes: Int? = 0
        get() = arguments?.getInt(titleKey)?.takeIf { it != 0 }

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    override fun bindView(): InputDialogLayoutBinding {
        return InputDialogLayoutBinding.inflate(provideLayoutInflater())
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        arguments?.getInt(inputTypeKey)?.let {
            binding.textInputEt.inputType = it
        }

        arguments?.getString(defaultInputKey)?.let {
            binding.textInputEt.setText(it)
        }

        if (arguments?.getBoolean(showPasteButtonKey) == true) {
            binding.pasteInputIv.setOnClickListener {
                binding.textInputEt.setText(requireContext().getClipBoardText())
            }
        } else {
            binding.pasteInputIv.visibility = View.GONE
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        binding.textInputEt.let {
            onInputDoneListener?.invoke(it.text.toString().trim())
        }
    }

}