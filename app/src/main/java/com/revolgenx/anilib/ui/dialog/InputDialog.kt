package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
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
        private const val hintTextKey = "hint_text_key"
        val tag = InputDialog::class.java.simpleName
        fun newInstance(
            title: Int? = null,
            inputType: Int? = null,
            default: String? = null,
            hint:Int? = null,
            showPasteButton: Boolean = true
        ): InputDialog {
            return InputDialog().also {
                it.arguments = bundleOf(
                    titleKey to title,
                    inputTypeKey to inputType,
                    defaultInputKey to default,
                    showPasteButtonKey to showPasteButton,
                    hintTextKey to hint
                )
            }
        }
    }

    var onInputDoneListener: ((str: String) -> Unit)? = null

    override var titleRes: Int? = 0
        get() = arguments?.getInt(titleKey)?.takeIf { it != 0 }

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    private val inputType:Int? get()= arguments?.getInt(inputTypeKey)?.takeIf { it != 0 }
    private val hint:Int? get()= arguments?.getInt(hintTextKey)?.takeIf { it != 0 }
    private val defaultInput:String? get()= arguments?.getString(defaultInputKey)
    private val showPasteButton:Boolean? get()= arguments?.getBoolean(showPasteButtonKey)

    override fun bindView(): InputDialogLayoutBinding {
        return InputDialogLayoutBinding.inflate(provideLayoutInflater)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        alertDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    override fun builder(dialogBuilder: DynamicDialog.Builder, savedInstanceState: Bundle?) {
        inputType?.let {
            binding.textInputEt.inputType = it
        }

        defaultInput?.let {
            binding.textInputEt.setText(it)
        }

        hint?.let {
            binding.textInputEt.setHint(it)
        }

        if (showPasteButton == true) {
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

    fun show(manager: FragmentManager) {
        super.show(manager, "InputTextDialog")
    }

}