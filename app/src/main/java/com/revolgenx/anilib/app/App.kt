package com.revolgenx.anilib.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import android.os.Build.VERSION_CODES.N_MR1
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.work.*
import com.facebook.common.logging.FLog
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.google.android.gms.ads.MobileAds
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.AppTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.app.theme.Constants
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.common.logger.AniLibDebugTree
import com.revolgenx.anilib.common.logger.LoggerTree
import com.revolgenx.anilib.common.repository.networkModules
import com.revolgenx.anilib.common.repository.repositoryModules
import com.revolgenx.anilib.notification.service.NotificationWorker
import com.revolgenx.anilib.infrastructure.service.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.viewmodel.viewModelModules
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.infrastructure.service.activityServiceModules
import com.revolgenx.anilib.social.ui.viewmodel.activityViewModelModules
import okhttp3.OkHttpClient


open class App : DynamicApplication() {
    companion object{
        var applicationContext:Context? = null
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        App.applicationContext = base
    }

    override fun onInitialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(LoggerTree())
        } else {
            Timber.plant(AniLibDebugTree(this))
        }
        setupFresco()
        setupNotificationWorker()
        startKoin {
            androidContext(this@App)
            modules(getKoinModules())
        }
        setupAds()
        setupAlMarkwon()
    }

    protected open fun getKoinModules() = listOf(
        viewModelModules,
        repositoryModules,
        networkModules,
        serviceModule,
        activityServiceModules,
        activityViewModelModules
    )


    private fun setupFresco() {
        val requestListeners: MutableSet<RequestListener> = HashSet()
        if (BuildConfig.DEBUG) {
            requestListeners.add(RequestLoggingListener())
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        }
        val config =
            OkHttpImagePipelineConfigFactory.newBuilder(context, OkHttpClient()) // other setters
                .setRequestListeners(requestListeners)
                .build()
        BigImageViewer.initialize(FrescoImageLoader.with(this.applicationContext, config))
    }


    private fun setupNotificationWorker() {
        if (loggedIn()) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val interval = when (load("notification_refresh_interval", "0")) {
                "0" -> 15
                "1" -> 20
                "2" -> 25
                "3" -> 30
                else -> 15
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


    private fun setupAds() {
        if (!disableAds()) {
            MobileAds.initialize(this.applicationContext)
        }
    }



    private fun setupAlMarkwon() {
        AlMarkwonFactory.init(this.applicationContext)
    }

    override fun getLocale(): Locale? {
        return Locale(getApplicationLocale())
    }

    override fun onCustomiseTheme() {
    }


    @StyleRes
    override fun getThemeRes(): Int {
        // Return application theme to be applied.
        return ThemeController.appStyle
    }

    override fun getDynamicTheme(): AppTheme<*>? {
        return ThemeController.dynamicAppTheme
    }


    @ColorInt
    override fun getDefaultColor(@Theme.ColorType colorType: Int): Int {
        return when (colorType) {
            Theme.ColorType.BACKGROUND -> {
                return ThemeController.backgroundColor
            }
            Theme.ColorType.ACCENT -> {
                ThemeController.accentColor
            }
            else -> super.getDefaultColor(colorType)
        }
    }

    override fun onDynamicChanged(context: Boolean, recreate: Boolean) {
        super.onDynamicChanged(context, recreate)

        if (recreate) {
            setDelayedTheme()
        }
    }

    private fun setDelayedTheme() {
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        // Update themes on shared preferences change.
        when (key) {
            Constants.PREF_SETTINGS_BACKGROUND_COLOR_LIGHT,
            Constants.PREF_SETTINGS_SURFACE_COLOR_LIGHT,
            Constants.PREF_SETTINGS_ACCENT_COLOR_LIGHT->{
                if(!ThemeController.isDarkMode){
                    DynamicTheme.getInstance().onDynamicChanged(false, true)
                }
            }
            Constants.PREF_SETTINGS_BACKGROUND_COLOR_DARK,
            Constants.PREF_SETTINGS_SURFACE_COLOR_DARK,
            Constants.PREF_SETTINGS_ACCENT_COLOR_DARK->{
                if(ThemeController.isDarkMode){
                    DynamicTheme.getInstance().onDynamicChanged(false, true)
                }
            }
            Constants.PREF_SETTINGS_CORNER_RADIUS,
            Constants.PREF_SETTINGS_CARD_ELEVATION,
            Constants.PREF_SETTINGS_THEME_MODE ->
                DynamicTheme.getInstance().onDynamicChanged(false, true)
            languagePrefKey -> {
                DynamicTheme.getInstance()
                    .onDynamicConfigurationChanged(true, false, false, false, false)
            }
        }
    }

}