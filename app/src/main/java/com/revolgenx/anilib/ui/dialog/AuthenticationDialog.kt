package com.revolgenx.anilib.ui.dialog

import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment

class AuthenticationDialog : BaseDialogFragment() {
    override var viewRes: Int? = R.layout.auth_dialog_layout
    override var dismissOnTouchOutside: Boolean = false
    companion object{
        fun newInstance() = AuthenticationDialog()
    }
}