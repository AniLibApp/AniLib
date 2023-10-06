package com.revolgenx.anilib.notification.data.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.Coil
import coil.request.ImageRequest
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.constant.LauncherShortcutKeys
import com.revolgenx.anilib.common.data.constant.LauncherShortcuts
import com.revolgenx.anilib.common.ui.theme.LightColorScheme
import com.revolgenx.anilib.common.util.immutableFlagUpdateCurrent
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.service.NotificationService
import com.revolgenx.anilib.notification.ui.model.ActivityNotificationModel
import com.revolgenx.anilib.notification.ui.model.AiringNotificationModel
import com.revolgenx.anilib.notification.ui.model.BaseNotificationModel
import com.revolgenx.anilib.notification.ui.model.FollowingNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaDataChangeNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaDeletionNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaMergeNotificationModel
import com.revolgenx.anilib.notification.ui.model.NotificationModel
import com.revolgenx.anilib.notification.ui.model.RelatedMediaNotificationModel
import com.revolgenx.anilib.notification.ui.model.ThreadNotificationModel
import com.revolgenx.anilib.type.NotificationType.*
import kotlinx.coroutines.flow.single
import java.util.Locale
import anilib.i18n.R as I18nR

data class NotificationData(val title: String, val image: String? = null)
class NotificationWorker(
    private val context: Context,
    params: WorkerParameters,
    private val notificationService: NotificationService
) : CoroutineWorker(context, params) {

    companion object {
        const val CHANNEL_ID = "com.revolgenx.anilib.notification.DEFAULT"
        const val CHANNEL_NAME = "AniLib"
        const val NOTIFICATION_ID = 102020
    }

    private val field = NotificationField(false).also { it.perPage = 1 }

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }


    override suspend fun doWork(): Result {
        return try {
            val notifications = notificationService.getNotificationList(field).single()
            notifications.data?.firstOrNull()?.let {
                showNotification(it)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun showNotification(item: NotificationModel) {
        val notificationData = getNotificationData(item) ?: return
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)

        notificationBuilder.setContentText(notificationData.title)
        notificationBuilder.setContentTitle(context.getString(I18nR.string.new_notification))
        val pendingIntent = createNotificationPendingIntent()
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setSmallIcon(
            R.drawable.ic_notification
        )
        notificationBuilder.setAutoCancel(true)
        notificationData.image?.let {
            val imageResult = Coil.imageLoader(context).execute(ImageRequest.Builder(context).data(it).build())
            imageResult.drawable?.let {
                notificationBuilder.setLargeIcon(it.toBitmap())
            }
        }
        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }


    private fun getNotificationData(item: BaseNotificationModel): NotificationData? {
        return when (item) {
            is ActivityNotificationModel -> createActivityNotification(item)
            is ThreadNotificationModel -> createThreadNotification(item)
            is AiringNotificationModel -> createAiringNotification(item)
            is FollowingNotificationModel -> createFollowingNotification(item)
            is RelatedMediaNotificationModel -> createRelatedMediaChangeNotification(item)
            is MediaDataChangeNotificationModel -> createMediaDataChangeNotification(item)
            is MediaMergeNotificationModel -> createMediaMergeNotification(item)
            is MediaDeletionNotificationModel -> createMediaDeleteNotification(item)
            else -> null
        }
    }

    private fun createThreadNotification(item: ThreadNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.thread_notif_s)
                .format(item.user?.name, item.context, item.thread?.title),
            image = item.user?.avatar?.image
        )
    }

    private fun createActivityNotification(item: ActivityNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.s_space_s)
                .format(item.user?.name, item.context),
            image = item.user?.avatar?.image
        )
    }

    private fun createAiringNotification(item: AiringNotificationModel): NotificationData {
        return NotificationData(
            title = String.format(
                Locale.getDefault(),
                context.getString(I18nR.string.episode_airing_notif),
                item.contexts!![0],
                item.episode,
                item.contexts[1],
                item.media?.title?.userPreferred,
                item.contexts[2]
            ),
            image = item.media?.coverImage?.image(MediaCoverImageModel.type_large)
        )
    }


    private fun createFollowingNotification(item: FollowingNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.s_space_s)
                .format(item.user?.name, item.context),
            image = item.user?.avatar?.image
        )
    }

    private fun createRelatedMediaChangeNotification(item: RelatedMediaNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.s_space_s)
                .format(item.media?.title?.userPreferred, item.context),
            image = item.media?.coverImage?.image(MediaCoverImageModel.type_large)
        )
    }


    private fun createMediaDataChangeNotification(item: MediaDataChangeNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.s_space_s)
                .format(item.media?.title?.userPreferred, item.context),
            image = item.media?.coverImage?.image(MediaCoverImageModel.type_large)
        )
    }


    private fun createMediaMergeNotification(item: MediaMergeNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.s_space_s)
                .format(item.media?.title?.userPreferred, item.context),
            image = item.media?.coverImage?.image(MediaCoverImageModel.type_large)
        )
    }

    private fun createMediaDeleteNotification(item: MediaDeletionNotificationModel): NotificationData {
        return NotificationData(
            title = context.getString(I18nR.string.s_space_s)
                .format(item.deletedMediaTitle, item.context),
        )
    }

    private fun createNotificationPendingIntent(): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, null, context, this::class.java).also {
            it.putExtra(
                LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                LauncherShortcuts.NOTIFICATION
            )
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            immutableFlagUpdateCurrent
        )
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val notificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(1000) /* ms */
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = LightColorScheme.primary.toArgb()
        notificationManagerCompat.createNotificationChannel(notificationChannel)
    }


}