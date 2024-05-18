package com.revolgenx.anilib.common.ui.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.common.data.event.CommonEvent
import com.revolgenx.anilib.common.data.event.EventBusListener
import com.revolgenx.anilib.common.data.event.OpenCharacterScreenEvent
import com.revolgenx.anilib.common.data.event.OpenImageEvent
import com.revolgenx.anilib.common.data.event.OpenLinkEvent
import com.revolgenx.anilib.common.data.event.OpenMediaScreenEvent
import com.revolgenx.anilib.common.data.event.OpenSpoilerEvent
import com.revolgenx.anilib.common.data.event.OpenUserScreenEvent
import com.revolgenx.anilib.common.data.event.registerForEvent
import com.revolgenx.anilib.common.data.event.unRegisterForEvent
import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.openLink
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.notification.data.worker.NotificationWorker
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


abstract class BaseMainActivity : ComponentActivity(), EventBusListener {
    protected val viewModel by viewModel<MainActivityViewModel>()
    protected var navigator: Navigator? = null
    private val themeDataStore: ThemeDataStore by inject()
    private val authDataStore: AuthPreferencesDataStore by inject()


    fun newIntent(context: Context) = Intent(context, this::class.java).apply {
        putExtra(
            "NOTIFICATION_MESSAGE_TAG", "Hi ☕\uD83C\uDF77\uD83C\uDF70"
        )
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


    fun sendNotification(context: Context) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = newIntent(context.applicationContext)
        // create a pending intent that opens MainActivity when the user clicks on the notification
        val stackBuilder = TaskStackBuilder.create(context)
            .addParentStack(this::class.java)
            .addNextIntent(intent)
        val notificationPendingIntent = stackBuilder
            .getPendingIntent(getUniqueId(), FLAG_IMMUTABLE)

//    build the notification object with the data to be shown
        val notification = NotificationCompat.Builder(context, NotificationWorker.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title")
            .setContentText("Content goes here")
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(getUniqueId(), notification)
    }

    private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupNotification()
    }

    private fun setupNotification() {
        createNotificationChannel()
        createNotificationWorker()
//        sendNotification(this)
    }

    private fun createNotificationWorker() {
        lifecycleScope.launch {
            authDataStore.isLoggedIn.flowWithLifecycle(lifecycle).collect { isLoggedIn ->
                if (isLoggedIn) {
                    val interval = 15
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
            }
        }

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

                is OpenUserScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.userScreen(userId, username)
                }

                is OpenLinkEvent -> {
                    openLink(link)
                }
            }
        }
    }

}