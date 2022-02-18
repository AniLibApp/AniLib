package com.revolgenx.anilib.util

import android.content.Context
import androidx.fragment.app.Fragment
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.ui.view.makeToast

fun Fragment.loginContinue(showLoginMsg: Boolean = true, callback: () -> Unit) {
    requireContext().loginContinue(showLoginMsg, callback)
}


fun Context.loginContinue(showLoginMsg: Boolean = true, callback: () -> Unit) {
    if (loggedIn()) {
        callback.invoke()
        return
    }
    if (showLoginMsg) {
        makeToast(R.string.please_log_in, null, R.drawable.ic_person)
    }
}
