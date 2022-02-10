package com.revolgenx.anilib.common.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.viewbinding.ViewBinding
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R


typealias OnShowListener = ((dialog: DynamicDialog, savedInstanceState: Bundle?) -> Unit)?
typealias OnButtonClickedListener = ((dialogInterface: DialogInterface, which: Int) -> Unit)?

abstract class BaseDialogFragment<V : ViewBinding> : DynamicDialogFragment() {

    protected open val titleRes: Int? = null
    protected open val positiveText: Int? = null
    protected open val negativeText: Int? = null

    protected open val neutralText: Int? = null
    protected open var messageText: Int? = null

    protected open val isAutoDismissEnabled = false
    protected open val dismissOnTouchOutside = true

    protected var positiveButton: Button? = null
    protected var titleTextView:TextView? = null

    companion object{
        const val DialogFragmentTag = "DialogFragmentTag"
    }

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

            builder(dialogBuilder, savedInstanceState)
        }
        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }


    //use for further building
    protected open fun builder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?){}


    protected val provideLayoutInflater: LayoutInflater get()= LayoutInflater.from(requireContext())


    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        with(alertDialog) {
            setOnShowListener {
                this.setCanceledOnTouchOutside(dismissOnTouchOutside)
                findViewById<TextView>(com.pranavpandey.android.dynamic.support.R.id.alertTitle)?.let {
                    titleTextView = it
                    it.typeface = ResourcesCompat.getFont(requireContext(), R.font.rubik_medium)
                }
                getButton(AlertDialog.BUTTON_POSITIVE)?.let {
                    positiveButton = it
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



    /** Show the bottom sheet. */
    fun show(ctx:Context) {
            when (ctx) {
                is FragmentActivity -> show(ctx.supportFragmentManager, DialogFragmentTag)
                is AppCompatActivity -> show(ctx.supportFragmentManager, DialogFragmentTag)
                is Fragment -> show(ctx.childFragmentManager, DialogFragmentTag)
                is PreferenceFragmentCompat -> show(ctx.childFragmentManager, DialogFragmentTag)
                is ContextThemeWrapper -> show(ctx.baseContext)
                is android.view.ContextThemeWrapper -> show(ctx.baseContext)
                else -> throw IllegalStateException("Context has no window attached.")
            }
    }

}