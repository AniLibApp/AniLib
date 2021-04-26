package com.revolgenx.anilib.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import android.os.Build.VERSION_CODES.N_MR1
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.multidex.MultiDex
import androidx.work.*
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.google.android.gms.ads.MobileAds
import com.pranavpandey.android.dynamic.support.DynamicApplication
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.app.theme.AppController
import com.revolgenx.anilib.app.theme.Constants
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.common.logger.AniLibDebugTree
import com.revolgenx.anilib.common.logger.LoggerTree
import com.revolgenx.anilib.infrastructure.repository.networkModules
import com.revolgenx.anilib.infrastructure.repository.repositoryModules
import com.revolgenx.anilib.infrastructure.service.notification.NotificationWorker
import com.revolgenx.anilib.infrastructure.service.serviceModule
import com.revolgenx.anilib.radio.repository.radioApiModules
import com.revolgenx.anilib.radio.repository.radioRoomModules
import com.revolgenx.anilib.radio.source.radioSourceModule
import com.revolgenx.anilib.ui.viewmodel.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.NotificationActivity
import com.revolgenx.anilib.activity.ToolbarContainerActivity
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.ui.fragment.notification.NotificationFragment
import com.revolgenx.anilib.util.LauncherShortcutKeys
import com.revolgenx.anilib.util.LauncherShortcuts
import com.revolgenx.anilib.util.shortcutAction


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

        if(!disableAds()){
            MobileAds.initialize(this)
        }

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

        setAppShortcuts()
    }

    private fun setAppShortcuts() {
        shortcutAction(this) {

            if (it.dynamicShortcuts.size != 0) return@shortcutAction

            val anilibShortcuts = mutableListOf<ShortcutInfo>()
            val homeShortcut = createShortcut(
                "home_shortcut",
                getString(R.string.home),
                getString(R.string.open_home_desc),
                R.drawable.ic_shortcut_home_filled,
                Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                    it.putExtra(
                        LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                        LauncherShortcuts.HOME.ordinal
                    )
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            )
            anilibShortcuts.add(homeShortcut)
            if (loggedIn()) {

                val animeShortcut = createShortcut(
                    "anime_shortcut",
                    getString(R.string.anime_list),
                    getString(R.string.open_anime_list),
                    R.drawable.ic_shortcut_laptop_chromebook,
                    Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                        it.putExtra(
                            LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                            LauncherShortcuts.ANIME.ordinal
                        )
                        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    }
                )
                val mangaShortcut = createShortcut(
                    "manga_shortcut",
                    getString(R.string.manga_list),
                    getString(R.string.open_manga_list),
                    R.drawable.ic_shortcut_menu_book,
                    Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                        it.putExtra(
                            LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                            LauncherShortcuts.MANGA.ordinal
                        )
                        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    }
                )

                anilibShortcuts.add(animeShortcut)
                anilibShortcuts.add(mangaShortcut)
            }


            val radioShortcut = createShortcut(
                "radio_shortcut",
                getString(R.string.radio),
                getString(R.string.open_radio),
                R.drawable.ic_shortcut_radio,
                Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                    it.putExtra(
                        LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                        LauncherShortcuts.RADIO.ordinal
                    )
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            )

            anilibShortcuts.add(radioShortcut)


            if (loggedIn()) {
                val notificationShortcut = createShortcut(
                    "notification_shortcut",
                    getString(R.string.notification),
                    getString(R.string.notification),
                    R.drawable.ic_shortcut_notifications,
                    Intent(Intent.ACTION_VIEW, null, this, NotificationActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    })

                anilibShortcuts.add(notificationShortcut)
            }


            it.dynamicShortcuts = anilibShortcuts
        }
    }

    @RequiresApi(N_MR1)
    private fun createShortcut(
        id: String,
        label: String,
        longLabel: String,
        @DrawableRes drawRes: Int,
        intent: Intent
    ): ShortcutInfo {
        return ShortcutInfo.Builder(this, id).setShortLabel(label).setLongLabel(longLabel)
            .setIcon(Icon.createWithResource(this, drawRes))
            .setIntent(intent)
            .build()
    }


    protected open fun getKoinModules() = listOf(
        viewModelModules,
        repositoryModules,
        networkModules,
        serviceModule,
        radioRoomModules,
        radioApiModules,
        radioSourceModule
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
            languagePrefKey -> {
                DynamicTheme.getInstance()
                    .onDynamicConfigurationChanged(true, false, false, false, false)
            }
        }
    }

}