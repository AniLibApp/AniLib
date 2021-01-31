package com.revolgenx.anilib.common.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R


typealias OnShowListener = ((dialog: DynamicDialog, savedInstanceState: Bundle?) -> Unit)?
typealias OnButtonClickedListener = ((dialogInterface: DialogInterface, which: Int) -> Unit)?

abstract class BaseDialogFragment<V : ViewBinding> : DynamicDialogFragment() {

    protected open var titleRes: Int? = null
    protected open var positiveText: Int? = null
    protected open var negativeText: Int? = null

    protected open var neutralText: Int? = null
    protected open var messageText: Int? = null

    protected open var isAutoDismissEnabled = false
    protected open var dismissOnTouchOutside = true

    private var _binding: V? = null
    val binding get() = _binding!!

    var onShowListener: OnShowListener = null
    var onButtonClickedListener: OnButtonClickedListener = null

    protected open fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        onButtonClickedListener?.invoke(dialogInterface, which)
    }

    protected open fun onNegativeClicked(dialogInterface: DialogInterface, which: Int) {
        onButtonClickedListener?.invoke(dialogInterface, which)
    }

    protected open fun onNeutralClicked(dialogInterface: DialogInterface, which: Int) {
        onButtonClickedListener?.invoke(dialogInterface, which)
    }

    protected open fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        onShowListener?.invoke(alertDialog, savedInstanceState)
    }

    protected abstract fun bindView(): V?

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            titleRes?.let {
                setTitle(it)
            }
            _binding = bindView()
            _binding?.let {
                setView(it.root)
            }
            messageText?.let {
                setMessage(it)
            }

            positiveText?.let {
                setPositiveButton(it) { dialog, which ->
                    onPositiveClicked(dialog, which)
                }
            }

            negativeText?.let {
                setNegativeButton(it) { dialog, which ->
                    onNegativeClicked(dialog, which)
                }
            }

            neutralText?.let {
                setNeutralButton(it) { dialog, which ->
                    onNeutralClicked(dialog, which)
                }
            }

            isAutoDismiss = isAutoDismissEnabled
        }
        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }


    protected fun provideLayoutInflater() = LayoutInflater.from(requireContext())


    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        with(alertDialog) {
            setOnShowListener {
                this.setCanceledOnTouchOutside(dismissOnTouchOutside)
                findViewById<TextView>(com.pranavpandey.android.dynamic.support.R.id.alertTitle)?.typeface =
                    ResourcesCompat.getFont(requireContext(), R.font.cabin_semi_bold)
                getButton(AlertDialog.BUTTON_POSITIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }

                getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }
                getButton(AlertDialog.BUTTON_NEUTRAL)?.let {
                    (it as DynamicButton).isAllCaps = false
                }

                onShowListener(alertDialog, savedInstanceState)
            }
        }
        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}