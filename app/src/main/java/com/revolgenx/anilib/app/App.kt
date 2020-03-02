package com.revolgenx.anilib.app

import android.content.Context
import android.os.Handler
import androidx.annotation.StyleRes
import androidx.multidex.MultiDex
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.repository.networkModules
import com.revolgenx.anilib.repository.repositoryModules
import com.revolgenx.anilib.viewmodel.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.*

class App : DynamicApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AppController.initializeInstance(this)
        MultiDex.install(this)
    }

    override fun onInitialize() {
        AndroidThreeTen.init(this)
        Fresco.initialize(this)
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@App)

            modules(listOf(viewModelModules, repositoryModules, networkModules))
        }
    }

    @StyleRes
    override fun getThemeRes(): Int {
        // Return application theme to be applied.
        return ThemeController.appStyle
    }


    override fun onCustomiseTheme() {
        ThemeController.setApplicationTheme()
    }


    override fun getLocale(): Locale? {
        return null
    }

}