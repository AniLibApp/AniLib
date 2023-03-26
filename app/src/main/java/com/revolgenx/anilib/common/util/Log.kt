package com.revolgenx.anilib.common.util

import android.util.Log
import timber.log.Timber

val ERROR = Log.ERROR
val DEBUG = Log.DEBUG

fun log(priority: Int = DEBUG, msg: () -> String) {
    when (priority) {
        DEBUG -> {
            Timber.d(msg())
        }
        ERROR -> {
            Timber.e(msg())
        }
    }
}