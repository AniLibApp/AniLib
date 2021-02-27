package com.revolgenx.anilib.ui.dialog

import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.AuthDialogLayoutBinding

class AuthenticationDialog : BaseDialogFragment<AuthDialogLayoutBinding>() {

    override var dismissOnTouchOutside: Boolean = false
    companion object{
        fun newInstance() = AuthenticationDialog()
    }

    override fun bindView(): AuthDialogLayoutBinding {
        return AuthDialogLayoutBinding.inflate(provideLayoutInflater)
    }
}