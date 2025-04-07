package com.revolgenx.anilib.common.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cafe.adriel.voyager.navigator.Navigator
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.app.ui.viewmodel.DeepLinkPath
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.common.data.constant.LauncherShortcutKeys
import com.revolgenx.anilib.common.data.constant.LauncherShortcuts
import com.revolgenx.anilib.common.data.event.CommonEvent
import com.revolgenx.anilib.common.data.event.EventBusListener
import com.revolgenx.anilib.common.data.event.OpenCharacterScreenEvent
import com.revolgenx.anilib.common.data.event.OpenImageEvent
import com.revolgenx.anilib.common.data.event.OpenLinkEvent
import com.revolgenx.anilib.common.data.event.OpenMediaScreenEvent
import com.revolgenx.anilib.common.data.event.OpenSpoilerEvent
import com.revolgenx.anilib.common.data.event.OpenStaffScreenEvent
import com.revolgenx.anilib.common.data.event.OpenStudioScreenEvent
import com.revolgenx.anilib.common.data.event.OpenUserScreenEvent
import com.revolgenx.anilib.common.data.event.registerForEvent
import com.revolgenx.anilib.common.data.event.unRegisterForEvent
import com.revolgenx.anilib.common.data.logger.AniLibDebugTree
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.notificationRefreshIntervalKey
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore.Companion.userIdKey
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.openUri
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.ads.AdsViewModel
import com.revolgenx.anilib.common.ui.ads.CheckAds
import com.revolgenx.anilib.common.ui.bottomsheet.WhatsNewBottomSheet
import com.revolgenx.anilib.common.util.versionName
import com.revolgenx.anilib.notification.data.worker.NotificationWorker
import com.revolgenx.anilib.setting.ui.viewmodel.BillingViewModel
import com.revolgenx.anilib.social.factory.MarkdownCallbackImpl
import com.revolgenx.anilib.social.factory.MarkdownFactoryImpl
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit


abstract class BaseMainActivity : AppCompatActivity(), EventBusListener {
    protected val viewModel by viewModel<MainActivityViewModel>()
    protected var navigator: Navigator? = null
    private val themeDataStore: ThemeDataStore by inject()
    protected val appPreferencesDataStore: AppPreferencesDataStore by inject()

    private val adsViewModel: AdsViewModel by viewModel<AdsViewModel>()
    private val billingViewModel: BillingViewModel by viewModel()


    companion object {
        const val WIDGET_MEDIA_ID_KEY = "widget_media_id_key"
        const val WIDGET_MEDIA_TYPE_KEY = "widget_media_type_key"
        const val WIDGET_AIRING_SCHEDULE_SCREEN_KEY = "widget_airing_schedule_screen_key"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(
                NotificationWorker.CHANNEL_ID,
                NotificationWorker.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        with(channel) {
            enableVibration(true)
            vibrationPattern = longArrayOf(1000) /* ms */
            enableLights(true)
            lightColor = themeDataStore.get().primary
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupLogger()
        setupNotification()
        setupMarkdown()
        checkIntent(intent)
        initBilling()
        initAds()
    }

    private fun setupLogger() {
        lifecycleScope.launch {
            appPreferencesDataStore.crashReport.collect {
                Timber.plant(AniLibDebugTree(it!!))
            }
        }
    }

    private fun setupMarkdown() {
        MarkdownFactoryImpl.initialize(
            this,
            themeDataStore.get().primary,
            appPreferencesDataStore.autoPlayGif.get()!!,
            MarkdownCallbackImpl()
        )
    }

    override fun onNewIntent(intent: Intent) {
        checkIntent(intent)
        super.onNewIntent(intent)
    }

    private fun checkIntent(intent: Intent?) {
        intent ?: return
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                //check shortcut intent
                if (intent.hasExtra(LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY)) {
                    val currentShortcut = intent.getIntExtra(
                        LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                        LauncherShortcuts.HOME.ordinal
                    )

                    when (LauncherShortcuts.entries[currentShortcut]) {
                        LauncherShortcuts.HOME -> {
                            viewModel.deepLinkPath.value = DeepLinkPath.HOME to true
                        }

                        LauncherShortcuts.ANIME -> {
                            viewModel.deepLinkPath.value = DeepLinkPath.ANIME_LIST to true
                        }

                        LauncherShortcuts.MANGA -> {
                            viewModel.deepLinkPath.value = DeepLinkPath.MANGA_LIST to true
                        }

                        LauncherShortcuts.NOTIFICATION -> {
                            viewModel.deepLinkPath.value = DeepLinkPath.NOTIFICATION to true
                        }
                    }
                }


                //check deeplink intent
                val data = intent.data ?: return
                val paths = data.pathSegments
                when (val urlPath = paths.getOrNull(0)) {
                    "user" -> {
                        val username = paths.getOrNull(1) ?: return
                        val userId = username.toIntOrNull()
                        viewModel.deepLinkPath.value = DeepLinkPath.USER to (userId ?: username)
                    }

                    "anime", "manga" -> {
                        val mediaId = paths.getOrNull(1)?.toIntOrNull() ?: return
                        viewModel.deepLinkPath.value = if (urlPath == "anime") {
                            DeepLinkPath.ANIME to mediaId
                        } else {
                            DeepLinkPath.MANGA to mediaId
                        }
                    }

                    "character" -> {
                        val characterId = paths.getOrNull(1)?.toIntOrNull() ?: return
                        viewModel.deepLinkPath.value = DeepLinkPath.CHARACTER to characterId
                    }

                    "staff" -> {
                        val staffId = paths.getOrNull(1)?.toIntOrNull() ?: return
                        viewModel.deepLinkPath.value = DeepLinkPath.STAFF to staffId
                    }

                    "activity" -> {
                        val activityId = paths.getOrNull(1)?.toIntOrNull() ?: return
                        viewModel.deepLinkPath.value = DeepLinkPath.ACTIVITY to activityId
                    }

                    "studio" -> {
                        val studioId = paths.getOrNull(1)?.toIntOrNull() ?: return
                        viewModel.deepLinkPath.value = DeepLinkPath.STUDIO to studioId
                    }
                }
            }
        }


        intent.takeIf { it.hasExtra(WIDGET_MEDIA_ID_KEY) }?.extras?.let { intentExtra ->
            val mediaId = intentExtra.getInt(WIDGET_MEDIA_ID_KEY)
            val mediaType = MediaType.entries[intentExtra.getInt(WIDGET_MEDIA_TYPE_KEY)]

            if (appPreferencesDataStore.widgetOpenListEditor.get() == true) {
                viewModel.deepLinkPath.value = DeepLinkPath.LIST_ENTRY_EDITOR to mediaId
            } else {
                viewModel.deepLinkPath.value = when (mediaType) {
                    MediaType.MANGA -> DeepLinkPath.MANGA to mediaId
                    else -> DeepLinkPath.ANIME to mediaId
                }
            }
            intent.removeExtra(WIDGET_MEDIA_ID_KEY)
            intent.removeExtra(WIDGET_MEDIA_TYPE_KEY)
        }

        intent.takeIf { it.hasExtra(WIDGET_AIRING_SCHEDULE_SCREEN_KEY) }?.let {
            viewModel.deepLinkPath.value = DeepLinkPath.AIRING to true
            intent.removeExtra(WIDGET_AIRING_SCHEDULE_SCREEN_KEY)
        }

        intent.action = ""
    }


    private fun setupNotification() {
        createNotificationChannel()
        createNotificationWorker()
    }

    private fun createNotificationWorker() {
        lifecycleScope.launch {
            appPreferencesDataStore.dataStore.data.flowWithLifecycle(lifecycle).collect {
                val isLoggedIn = it[userIdKey] != null

                if (isLoggedIn) {
                    val interval = it[notificationRefreshIntervalKey] ?: 15
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val periodicWork =
                        PeriodicWorkRequestBuilder<NotificationWorker>(
                            interval.toLong(),
                            TimeUnit.MINUTES
                        )
                            .setConstraints(constraints)
                            .build()

                    WorkManager.getInstance(this@BaseMainActivity).enqueueUniquePeriodicWork(
                        NotificationWorker.NOTIFICATION_WORKER_TAG,
                        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                        periodicWork
                    )
                } else {
                    WorkManager.getInstance(this@BaseMainActivity)
                        .cancelUniqueWork(NotificationWorker.NOTIFICATION_WORKER_TAG)
                }
                setAppShortcuts(isLoggedIn = isLoggedIn)
            }
        }

    }


    private fun setAppShortcuts(isLoggedIn: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        val it = getSystemService(ShortcutManager::class.java) ?: return

        val anilibShortcuts = mutableListOf<ShortcutInfo>()
        val homeShortcut = createShortcut(
            "home_shortcut",
            getString(anilib.i18n.R.string.home),
            R.drawable.ic_shortcut_home,
            Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                it.putExtra(
                    LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                    LauncherShortcuts.HOME.ordinal
                )
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        )
        anilibShortcuts.add(homeShortcut)

        if (isLoggedIn) {

            val animeShortcut = createShortcut(
                "anime_shortcut",
                getString(anilib.i18n.R.string.settings_anime_list),
                R.drawable.ic_shortcut_computer,
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
                getString(anilib.i18n.R.string.settings_manga_list),
                R.drawable.ic_shortcut_book,
                Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                    it.putExtra(
                        LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                        LauncherShortcuts.MANGA.ordinal
                    )
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            )

            val notificationShortcut = createShortcut(
                "notification_shortcut",
                getString(anilib.i18n.R.string.notifications),
                R.drawable.ic_shortcut_notification,
                Intent(Intent.ACTION_VIEW, null, this, MainActivity::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    it.putExtra(
                        LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                        LauncherShortcuts.NOTIFICATION.ordinal
                    )
                })

            anilibShortcuts.add(animeShortcut)
            anilibShortcuts.add(mangaShortcut)
            anilibShortcuts.add(notificationShortcut)
        }

        it.dynamicShortcuts = anilibShortcuts
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun createShortcut(
        id: String,
        label: String,
        @DrawableRes drawRes: Int,
        intent: Intent
    ): ShortcutInfo {
        return ShortcutInfo.Builder(this, id)
            .setShortLabel(label)
            .setLongLabel(label)
            .setIcon(Icon.createWithResource(this, drawRes))
            .setIntent(intent)
            .build()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CommonEvent) {
        event.apply {
            when (this) {
                is OpenImageEvent -> {
                    imageUrl ?: return
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.imageViewerScreen(imageUrl)
                }

                is OpenSpoilerEvent -> {
                    viewModel.spoilerSpanned = spanned
                    viewModel.openSpoilerBottomSheet.value = true
                }

                is OpenMediaScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.mediaScreen(mediaId, type)
                }

                is OpenCharacterScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.characterScreen(characterId)
                }

                is OpenStaffScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.staffScreen(staffId)
                }

                is OpenStudioScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.studioScreen(studioId)
                }

                is OpenUserScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.userScreen(userId, username)
                }

                is OpenLinkEvent -> {
                    openUri(link)
                }
            }
        }
    }

    private fun initAds() {
        adsViewModel.initAds(this)
    }

    private fun initBilling(){
        billingViewModel.setupBillingClient(this)
    }

    @Composable
    fun CheckWhatsNew() {
        val bottomSheetState = rememberBottomSheetState()
        LaunchedEffect(viewModel) {
            if (viewModel.currentAppVersion.get() != versionName) {
                viewModel.currentAppVersion.set(versionName)
                bottomSheetState.peek()
            }
        }

        WhatsNewBottomSheet(bottomSheetState = bottomSheetState)
    }

    @Composable
    fun CanShowAds() {
        CheckAds(
            navigator = navigator,
            activity = this,
            viewModel = viewModel,
            adsViewModel = adsViewModel
        )
    }

    override fun onDestroy() {
        MarkdownFactoryImpl.destroy()
        super.onDestroy()
    }

}