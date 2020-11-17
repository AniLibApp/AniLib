package com.revolgenx.anilib.common.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class LoggerTree: Timber.DebugTree(){
    init {
        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(false)
    }
}
