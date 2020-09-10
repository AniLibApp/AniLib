package com.revolgenx.anilib.dialog

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.revolgenx.anilib.R

class AuthenticationDialog : BaseDialogFragment() {
    override var viewRes: Int? = R.layout.auth_dialog_layout
    override var dismissOnTouchOutside: Boolean = false
    companion object{
        fun newInstance() = AuthenticationDialog()
    }
}