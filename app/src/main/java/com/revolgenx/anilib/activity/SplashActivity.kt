package com.revolgenx.anilib.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.work.*
import com.facebook.common.logging.FLog
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.google.android.gms.ads.MobileAds
import com.pranavpandey.android.dynamic.support.splash.activity.DynamicSplashActivity
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.common.logger.LoggerTree
import com.revolgenx.anilib.common.preference.disableAds
import com.revolgenx.anilib.common.preference.getApplicationLocale
import com.revolgenx.anilib.common.preference.getString
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.notification.service.NotificationWorker
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.util.LauncherShortcutKeys
import com.revolgenx.anilib.util.LauncherShortcuts
import com.revolgenx.anilib.util.shortcutAction
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Implementing a splash screen by using [DynamicSplashActivity].
 */
class SplashActivity : DynamicSplashActivity() {

    /**
     * Splash image view to start animations.
     */
    private var mSplash: AppCompatImageView? = null

    override fun getLocale(): Locale {
        return Locale(getApplicationLocale())
    }

    override fun setNavigationBarThemeInLandscape(): Boolean {
        return true
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun getMinSplashTime(): Long {
        return 550
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onViewCreated(view: View) {
        mSplash = view.findViewById(R.id.splash_image)
    }


    override fun getStatusBarColor(): Int {
        return dynamicBackgroundColor
    }

    override fun onPreSplash() {
    }

    override fun doBehindSplash() {
        setupNotificationWorker()
        setupAlMarkwon()
        setupFresco()
        setupAds()
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
                    Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        it.putExtra(
                            LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                            LauncherShortcuts.NOTIFICATION.ordinal
                        )
                    })

                anilibShortcuts.add(notificationShortcut)
            }


            it.dynamicShortcuts = anilibShortcuts
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
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


    private fun setupAds() {
        if (!disableAds()) {
            MobileAds.initialize(this.applicationContext)
        }
    }

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

    private fun setupAlMarkwon() {
        AlMarkwonFactory.init(this.applicationContext)
    }

    private fun setupNotificationWorker() {
        if (context.loggedIn()) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val interval = when (context.getString("notification_refresh_interval", "0")) {
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

    override fun onPostSplash() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
