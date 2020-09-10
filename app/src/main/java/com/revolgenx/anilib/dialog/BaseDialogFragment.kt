package com.revolgenx.anilib.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R


typealias OnShowListener = ((dialog: DynamicDialog, savedInstanceState: Bundle?) -> Unit)?
typealias OnButtonClickedListener = ((dialogInterface: DialogInterface, which: Int) -> Unit)?

open class BaseDialogFragment : DynamicDialogFragment() {

    protected open var titleRes: Int? = null
    protected open var viewRes: Int? = null
    protected open var positiveText: Int? = null
    protected open var negativeText: Int? = null

    protected open var neutralText: Int? = null
    protected open var messageText: Int? = null

    protected open var isAutoDismissEnabled = false
    protected open var dismissOnTouchOutside = true

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

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            titleRes?.let {
                setTitle(it)
            }
            viewRes?.let {
                setView(it)
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


    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        with(alertDialog) {
            setOnShowListener {
                this.setCanceledOnTouchOutside(dismissOnTouchOutside)
                findViewById<TextView>(com.pranavpandey.android.dynamic.support.R.id.alertTitle)?.typeface =
                    ResourcesCompat.getFont(requireContext(), R.font.berlinrounded_extra_bold)
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
}