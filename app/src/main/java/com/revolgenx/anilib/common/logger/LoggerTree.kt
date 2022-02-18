package com.revolgenx.anilib.common.logger

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.revolgenx.anilib.BuildConfig
import timber.log.Timber

@SuppressLint("LogNotTimber")
class LoggerTree: Timber.DebugTree(){
    init {
        Log.i(LoggerTree::class.java.simpleName,"Flavor-${BuildConfig.FLAVOR}")
        if(BuildConfig.FLAVOR != "dev"){
            Firebase.crashlytics
                .setCrashlyticsCollectionEnabled(true)
            Firebase.analytics
                .setAnalyticsCollectionEnabled(true)
        }
    }
}
