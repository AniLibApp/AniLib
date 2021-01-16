package com.revolgenx.anilib.ui.dialog

import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.LoadingDialogLayoutBinding

object LoadingDialog {
    fun createLoadingDialog(
        customMessage: String? = null
    ): DialogFragment {
        return MessageDialog.Companion.Builder().let {
            it.view = R.layout.loading_dialog_layout
            val dialog = it.build()
            if (customMessage != null) {
                dialog.setOnShowListener {
                    dialog.view?.findViewById<TextView>(R.id.loadingDialogTv)?.text = customMessage
                }
            }
            dialog
        }
    }
}