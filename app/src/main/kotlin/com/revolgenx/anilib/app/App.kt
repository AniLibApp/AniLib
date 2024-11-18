package com.revolgenx.anilib.app

import android.app.Application
import coil.Coil
import coil.util.DebugLogger
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.data.repository.repositoryModules
import com.revolgenx.anilib.common.data.service.serviceModules
import com.revolgenx.anilib.common.data.store.storeModules
import com.revolgenx.anilib.common.data.worker.workerModules
import com.revolgenx.anilib.common.ui.viewmodel.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {
    private val koinMoules = listOf(
        repositoryModules,
        serviceModules,
        storeModules,
        viewModelModules,
        workerModules
    )

    override fun onCreate() {

        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(koinMoules)
        }

        Coil.setImageLoader(
            Coil.imageLoader(this).newBuilder().crossfade(true).apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }.build()
        )
    }


}