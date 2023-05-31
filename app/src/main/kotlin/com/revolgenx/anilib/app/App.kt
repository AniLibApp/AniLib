package com.revolgenx.anilib.app

import android.app.Application
import com.revolgenx.anilib.common.data.repository.repositoryModules
import com.revolgenx.anilib.common.data.service.serviceModules
import com.revolgenx.anilib.common.data.store.storeModules
import com.revolgenx.anilib.common.ui.viewmodel.viewModelModules
import com.revolgenx.anilib.social.factory.AlMarkdownCallbackImpl
import com.revolgenx.anilib.social.factory.AlMarkdownFactory
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
        startKoin {
            androidContext(this@App)
            modules(koinMoules)
        }
        AlMarkdownFactory.init(this, AlMarkdownCallbackImpl())
    }
}