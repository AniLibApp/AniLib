package com.revolgenx.anilib.common.logger

import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.revolgenx.anilib.common.preference.isCrashReportEnabled
import timber.log.Timber

class AniLibDebugTree(context: Context) : Timber.DebugTree() {
    private val priorityKey = "priority"
    private val tagKey = "tag"
    private val messageKey = "message"

    init {
        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(isCrashReportEnabled(context))
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        val t = throwable ?: Exception(message)

        FirebaseCrashlytics.getInstance().setCustomKey(priorityKey, priority)
        FirebaseCrashlytics.getInstance().setCustomKey(tagKey, tag ?: "tag")
        FirebaseCrashlytics.getInstance().setCustomKey(messageKey, message)
        FirebaseCrashlytics.getInstance().recordException(t)
    }

}