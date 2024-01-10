package com.revolgenx.anilib.app

import android.app.Application
import coil.Coil
import com.revolgenx.anilib.common.data.repository.repositoryModules
import com.revolgenx.anilib.common.data.service.serviceModules
import com.revolgenx.anilib.common.data.store.ThemePreferencesDataStore
import com.revolgenx.anilib.common.data.store.storeModules
import com.revolgenx.anilib.common.data.worker.workerModules
import com.revolgenx.anilib.common.ui.viewmodel.viewModelModules
import com.revolgenx.anilib.social.factory.AlMarkdownCallbackImpl
import com.revolgenx.anilib.social.factory.AlMarkdownFactory
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    private val themePreferencesDataStore by inject<ThemePreferencesDataStore>()
    private val koinMoules = listOf(
        repositoryModules,
        serviceModules,
        viewModelModules,
        storeModules,
        workerModules
    )

    override fun onCreate() {

        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(koinMoules)
        }

        Timber.plant(Timber.DebugTree())
        AlMarkdownFactory.init(this, AlMarkdownCallbackImpl())
        Coil.setImageLoader(Coil.imageLoader(this).newBuilder().crossfade(true).build())
    }


}