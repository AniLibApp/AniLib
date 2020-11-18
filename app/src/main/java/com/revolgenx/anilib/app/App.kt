package com.revolgenx.anilib.app

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StyleRes
import androidx.multidex.MultiDex
import androidx.work.*
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.app.theme.AppController
import com.revolgenx.anilib.app.theme.Constants
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.common.logger.AniLibDebugTree
import com.revolgenx.anilib.common.logger.LoggerTree
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.preference.getString
import com.revolgenx.anilib.common.preference.languagePrefKey
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.infrastructure.repository.networkModules
import com.revolgenx.anilib.infrastructure.repository.repositoryModules
import com.revolgenx.anilib.infrastructure.service.notification.NotificationWorker
import com.revolgenx.anilib.infrastructure.service.serviceModule
import com.revolgenx.anilib.ui.viewmodel.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


open class App : DynamicApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AppController.initializeInstance(this)
        MultiDex.install(this)
    }

    override fun onInitialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(LoggerTree())
        } else {
            Timber.plant(AniLibDebugTree(this))
        }

        AndroidThreeTen.init(this)

        val requestListeners: MutableSet<RequestListener> = HashSet()
        requestListeners.add(RequestLoggingListener())
        val config = ImagePipelineConfig.newBuilder(context) // other setters
            .setRequestListeners(requestListeners)
            .build()
        BigImageViewer.initialize(FrescoImageLoader.with(this, config))
        startKoin {
            androidContext(this@App)
            modules(getKoinModules())
        }


        if (context.loggedIn()) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val interval = when (context.getString("notification_refresh_interval", "0")) {
                "0" -> {
                    15
                }
                "1" -> {
                    20
                }
                "2" -> {
                    25
                }
                "3" -> {
                    30
                }
                else -> {
                    15
                }
            }
            val periodicWork =
                PeriodicWorkRequestBuilder<NotificationWorker>(interval.toLong(), TimeUnit.MINUTES)
                    .setConstraints(constraints).build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                NotificationWorker.NOTIFICATION_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork
            )
        } else {
            WorkManager.getInstance(this)
                .cancelUniqueWork(NotificationWorker.NOTIFICATION_WORKER_TAG)
        }
    }

    protected open fun getKoinModules() = listOf(
        viewModelModules,
        repositoryModules,
        networkModules,
        serviceModule
    )

    @StyleRes
    override fun getThemeRes(): Int {
        // Return application theme to be applied.
        return ThemeController.appStyle
    }


    override fun onCustomiseTheme() {
        ThemeController.setApplicationTheme()
    }


    override fun getLocale(): Locale? {
        return Locale(getApplicationLocale())
    }

    override fun onDynamicChanged(context: Boolean, recreate: Boolean) {
        super.onDynamicChanged(context, recreate)
        if (context) {
            AppController.instance.context = this
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        // Update themes on shared preferences change.
        when (key) {
            Constants.PREF_SETTINGS_APP_THEME_DAY_COLOR ->
                if (!DynamicTheme.getInstance().isNight && ThemeController.isAutoTheme) {
                    DynamicTheme.getInstance().onDynamicChanged(false, true)
                }
            Constants.PREF_SETTINGS_APP_THEME_NIGHT_COLOR ->
                if (DynamicTheme.getInstance().isNight && ThemeController.isAutoTheme) {
                    DynamicTheme.getInstance().onDynamicChanged(false, true)
                }
            Constants.PREF_SETTINGS_APP_THEME_COLOR,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_PRIMARY,
            Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT ->
                DynamicTheme.getInstance().onDynamicChanged(false, true)
            Constants.PREF_SETTINGS_NAVIGATION_BAR_THEME ->
                DynamicTheme.getInstance().onNavigationBarThemeChanged()
            languagePrefKey -> {
                DynamicTheme.getInstance().onDynamicConfigurationChanged(true, false, false, false, false)
            }
        }
    }

}