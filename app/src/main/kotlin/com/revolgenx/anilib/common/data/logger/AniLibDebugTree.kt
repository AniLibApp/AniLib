package com.revolgenx.anilib.common.data.logger

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import timber.log.Timber
import com.revolgenx.anilib.BuildConfig

class AniLibDebugTree(enableCrashReport: Boolean): Timber.DebugTree(){
    private val priorityKey = "priority"
    private val tagKey = "tag"
    private val messageKey = "message"

    init {
        if(BuildConfig.FLAVOR != "dev"){
            val enableCrashlytics = if(BuildConfig.DEBUG) true else enableCrashReport

            Firebase.crashlytics
                .setCrashlyticsCollectionEnabled(enableCrashlytics)
            Firebase.analytics
                .setAnalyticsCollectionEnabled(true)
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if(BuildConfig.DEBUG){
            super.log(priority, tag, message, t)
        }

        if (BuildConfig.FLAVOR == "dev" || priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        val exception = t ?: Exception(message)

        Firebase.crashlytics.apply {
            setCustomKey(priorityKey, priority)
            setCustomKey(tagKey, tag ?: "tag")
            setCustomKey(messageKey, message)
            recordException(exception)
        }

    }
}

//class DebugLoggerTree: Timber.DebugTree(){
//    init {
//        Log.i(LoggerTree::class.java.simpleName,"Flavor-${BuildConfig.FLAVOR}")
//        doIfNotDevFlavor {
//            Firebase.crashlytics
//                .setCrashlyticsCollectionEnabled(true)
//            Firebase.analytics
//                .setAnalyticsCollectionEnabled(true)
//        }
//    }
//}