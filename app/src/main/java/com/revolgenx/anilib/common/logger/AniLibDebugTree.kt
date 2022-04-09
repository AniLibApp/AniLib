package com.revolgenx.anilib.common.logger

import android.content.Context
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.preference.isCrashReportEnabled
import com.revolgenx.anilib.util.doIfNotDevFlavor
import timber.log.Timber

class AniLibDebugTree(context: Context) : Timber.DebugTree() {
    private val priorityKey = "priority"
    private val tagKey = "tag"
    private val messageKey = "message"

    init {
        doIfNotDevFlavor {
            Firebase.crashlytics
                .setCrashlyticsCollectionEnabled(isCrashReportEnabled())
            Firebase.analytics
                .setAnalyticsCollectionEnabled(true)
        }
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (BuildConfig.FLAVOR == "dev" || priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        val t = throwable ?: Exception(message)

        Firebase.crashlytics.apply {
            setCustomKey(priorityKey, priority)
            setCustomKey(tagKey, tag ?: "tag")
            setCustomKey(messageKey, message)
            recordException(t)
        }
    }

}