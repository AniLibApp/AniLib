package com.revolgenx.anilib.common.logger

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.revolgenx.anilib.BuildConfig
import timber.log.Timber

class LoggerTree: Timber.DebugTree(){
    init {
        if(BuildConfig.FLAVOR != "dev"){
            Firebase.crashlytics
                .setCrashlyticsCollectionEnabled(false)
        }
    }
}
