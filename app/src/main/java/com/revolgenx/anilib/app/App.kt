package com.revolgenx.anilib.app

import android.app.Application
import com.facebook.common.logging.FLog
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.data.repository.repositoryModules
import com.revolgenx.anilib.common.data.service.serviceModules
import com.revolgenx.anilib.common.data.store.storeModules
import com.revolgenx.anilib.common.ui.screen.viewModelModules
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    private val koinMoules = listOf(
        repositoryModules,
        serviceModules,
        viewModelModules,
        storeModules
    )

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupFresco()
        startKoin {
            androidContext(this@App)
            modules(koinMoules)
        }
    }

    private fun setupFresco() {
        val requestListeners: MutableSet<RequestListener> = HashSet()
        if (BuildConfig.DEBUG) {
            requestListeners.add(RequestLoggingListener())
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        }
        val pipelineConfig =
            OkHttpImagePipelineConfigFactory
                .newBuilder(this, OkHttpClient())
                .setRequestListeners(requestListeners)
                .build()
        Fresco.initialize(this, pipelineConfig)
    }
}