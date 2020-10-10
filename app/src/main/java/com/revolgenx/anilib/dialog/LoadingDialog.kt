package com.revolgenx.anilib.dialog

import androidx.fragment.app.DialogFragment
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.loading_dialog_layout.*

object LoadingDialog {
    fun createLoadingDialog(
        customMessage: String? = null
    ): DialogFragment {
        return MessageDialog.Companion.Builder().let {
            it.view = R.layout.loading_dialog_layout
            val dialog = it.build()
            if (customMessage != null) {
                dialog.setOnShowListener {
                    dialog.loadingDialogTv.text = customMessage
                }
            }
            dialog
        }
    }
}