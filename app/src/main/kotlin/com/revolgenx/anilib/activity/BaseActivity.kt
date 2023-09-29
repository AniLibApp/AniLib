package com.revolgenx.anilib.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.event.CommonEvent
import com.revolgenx.anilib.common.data.event.EventBusListener
import com.revolgenx.anilib.common.data.event.OpenCharacterScreenEvent
import com.revolgenx.anilib.common.data.event.OpenImageEvent
import com.revolgenx.anilib.common.data.event.OpenMediaScreenEvent
import com.revolgenx.anilib.common.data.event.OpenSpoilerEvent
import com.revolgenx.anilib.common.data.event.OpenUserScreenEvent
import com.revolgenx.anilib.common.data.event.registerForEvent
import com.revolgenx.anilib.common.data.event.unRegisterForEvent
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.theme.LightColorScheme
import com.revolgenx.anilib.notification.data.worker.NotificationWorker
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import anilib.i18n.R as I18n


abstract class BaseActivity : ComponentActivity(), EventBusListener {
    protected val viewModel by viewModel<MainActivityViewModel>()
    protected var navigator: Navigator? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
            sendNotification(this)
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }


    val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"
    val NOTIFICATION_MESSAGE_TAG = "message from notification"

    fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
        putExtra(
            NOTIFICATION_MESSAGE_TAG, "Hi ☕\uD83C\uDF77\uD83C\uDF70"
        )
    }


    fun sendNotification(context: Context) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // We need to create a NotificationChannel associated with our CHANNEL_ID before sending a notification.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
        ) {
            val name = context.getString(I18n.string.app_name)
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = newIntent(context.applicationContext)

        // create a pending intent that opens MainActivity when the user clicks on the notification
        val stackBuilder = TaskStackBuilder.create(context)
            .addParentStack(MainActivity::class.java)
            .addNextIntent(intent)
        val notificationPendingIntent = stackBuilder
            .getPendingIntent(getUniqueId(), FLAG_IMMUTABLE)

//    build the notification object with the data to be shown
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
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

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val notificationChannel =
            NotificationChannel(
                NotificationWorker.CHANNEL_ID,
                NotificationWorker.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(1000) /* ms */
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = LightColorScheme.primary.toArgb()
        notificationManagerCompat.createNotificationChannel(notificationChannel)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        createChannel()

//        when{
//            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
//                sendNotification(this)
//            }
//
////            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
////                Snackbar.make(
////                    findViewById(R.id.parent_layout),
////                    "Notification blocked",
////                    Snackbar.LENGTH_LONG
////                ).setAction("Settings") {
////                    // Responds to click on the action
////                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                    val uri: Uri = Uri.fromParts("package", packageName, null)
////                    intent.data = uri
////                    startActivity(intent)
////                }.show()
////            }
//            else -> {
//                // The registered ActivityResultCallback gets the result of this request
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    requestPermissionLauncher.launch(
//                        Manifest.permission.POST_NOTIFICATIONS
//                    )
//                }
//            }
//        }
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
            }
        }
    }

}