package com.revolgenx.anilib.dialog

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.revolgenx.anilib.R

class AuthDialog : DynamicDialogFragment() {

    companion object{
        fun newInstance() = AuthDialog()
    }

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {

        dialogBuilder.setView(R.layout.auth_dialog_layout)
        dialogBuilder.setTitle(R.string.login_service)
        return dialogBuilder
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }
}