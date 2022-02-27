package com.revolgenx.anilib.notification.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.CloseableStaticBitmap
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.notification.data.model.FollowingNotificationModel
import com.revolgenx.anilib.notification.data.model.media.MediaDataChangeNotificationModel
import com.revolgenx.anilib.notification.data.model.media.MediaDeletionNotificationModel
import com.revolgenx.anilib.notification.data.model.media.MediaMergeNotificationModel
import com.revolgenx.anilib.notification.data.model.media.RelatedMediaNotificationModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.model.NotificationModel
import com.revolgenx.anilib.notification.data.model.activity.*
import com.revolgenx.anilib.notification.data.model.thread.*
import com.revolgenx.anilib.util.LauncherShortcutKeys
import com.revolgenx.anilib.util.LauncherShortcuts
import com.revolgenx.anilib.util.getPendingIntentUpdateFlag
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class NotificationWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params), KoinComponent {

    companion object {
        const val CHANNEL_ID = "com.revolgenx.anilib.notification.CHANNEL_ID"
        const val CHANNEL_NAME = "AniLib"
        const val NOTIFICATION_ID = 102020

        const val NOTIFICATION_WORKER_TAG = "ANILIB_NOTIFICATION_WORKER_TAG"
    }

    private val notificationService: NotificationService by inject()
    private val compositeDisposable = CompositeDisposable()
    private val notificationField = NotificationField().also {
        it.resetNotificationCount = false
        it.perPage = 1
    }

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }

    private var pendingIntent: PendingIntent? = null
    private var notificationImage: String? = null

    override fun doWork(): Result {
        return runBlocking {
            if (context.loggedIn()) {
                val suspendedQuery =
                    suspendCoroutine<Resource<List<NotificationModel>>> { continuation ->
                        notificationService.getNotifications(
                            notificationField,
                            compositeDisposable
                        ) {
                            continuation.resume(it)
                        }
                    }

                if (suspendedQuery.status == Status.SUCCESS && suspendedQuery.data != null) {
                    val item = suspendedQuery.data
                    if (isNewNotification(item.firstOrNull())) {
                        setNewNotification(item.firstOrNull())
                        createChannel()
                        showNotification(item.firstOrNull())
                    }
                }
            }

            return@runBlocking Result.success()
        }
    }


    private fun showNotification(item: NotificationModel?) {
        if (item == null) return

        val notificationText = getNotificationString(item)
        if (notificationText.isEmpty()) return

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)

        notificationBuilder.setContentText(notificationText)
        notificationBuilder.setContentTitle(context.getString(R.string.new_notification))
        createNotificationPendingIntent()
        pendingIntent?.let {
            notificationBuilder.setContentIntent(it)
        }
        notificationBuilder.setSmallIcon(
            R.drawable.ic_notification_filled
        )
        notificationBuilder.setAutoCancel(true)
        notificationImage?.let {
            val dataSource = Fresco.getImagePipeline().fetchDecodedImage(
                ImageRequestBuilder.newBuilderWithSource(it.toUri())
                    .setImageDecodeOptions(
                        ImageDecodeOptions.newBuilder()
                            .setForceStaticImage(true).build()
                    )
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build(),
                context
            )
            try {
                val result =
                    DataSources.waitForFinalResult<CloseableReference<CloseableImage>>(dataSource)
                notificationBuilder.setLargeIcon(
                    if (result != null) {
                        (result.get() as CloseableStaticBitmap).underlyingBitmap.copy(
                            (result.get() as CloseableStaticBitmap).underlyingBitmap.config,
                            true
                        )
                    } else {
                        BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
                    }
                )
            } finally {
                dataSource.close()
            }
        }

        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationString(item: NotificationModel): String {
        return when (item.notificationUnionType) {
            NotificationUnionType.ACTIVITY_MESSAGE -> createActivityNotif(item as ActivityMessageNotification)
            NotificationUnionType.ACTIVITY_REPLY -> createActivityNotif(item as ActivityReplyNotification)
            NotificationUnionType.ACTIVITY_MENTION -> createActivityNotif(item as ActivityMentionNotification)
            NotificationUnionType.ACTIVITY_LIKE -> createActivityNotif(item as ActivityLikeNotification)
            NotificationUnionType.ACTIVITY_REPLY_LIKE -> createActivityNotif(item as ActivityReplyLikeNotification)
            NotificationUnionType.ACTIVITY_REPLY_SUBSCRIBED -> createActivityNotif(item as ActivityReplySubscribedNotification)
            NotificationUnionType.THREAD_COMMENT_MENTION -> createThreadNotif(item as ThreadCommentMentionNotification)
            NotificationUnionType.THREAD_SUBSCRIBED -> createThreadNotif(item as ThreadCommentSubscribedNotification)
            NotificationUnionType.THREAD_COMMENT_REPLY -> createThreadNotif(item as ThreadCommentReplyNotification)
            NotificationUnionType.THREAD_LIKE -> createThreadNotif(item as ThreadLikeNotification)
            NotificationUnionType.THREAD_COMMENT_LIKE -> createThreadNotif(item as ThreadCommentLikeNotification)
            NotificationUnionType.AIRING -> {
                (item as AiringNotificationModel).let {
                    notificationImage = it.media?.coverImage?.image(context)
                    String.format(
                        Locale.getDefault(),
                        context.getString(R.string.episode_airing_notif),
                        it.contexts!![0],
                        it.episode,
                        it.contexts!![1],
                        it.media?.title?.title(context),
                        it.contexts!![2]
                    )
                }
            }

            NotificationUnionType.FOLLOWING -> createFollowingNotification(item as FollowingNotificationModel)
            NotificationUnionType.RELATED_MEDIA_ADDITION -> {
                if (!getNotificationPreference(context.getString(R.string.related_media_added_notif))) return ""

                (item as RelatedMediaNotificationModel)
                notificationImage = item.media?.coverImage?.image
                return context.getString(R.string.s_space_s)
                    .format(item.media?.title?.title(context), item.context)
            }

            NotificationUnionType.MEDIA_DATA_CHANGE -> createMediaDataChangeNotification(item as MediaDataChangeNotificationModel)
            NotificationUnionType.MEDIA_MERGE -> createMediaMergeNotification(item as MediaMergeNotificationModel)
            NotificationUnionType.MEDIA_DELETION -> createMediaDeleteNotification(item as MediaDeletionNotificationModel)
            else -> ""
        }

    }

    private fun createThreadNotif(item: ThreadNotification): String {
        notificationImage = item.user?.avatar?.image
        return context.getString(R.string.thread_notif_s)
            .format(item.user?.name, item.context, item.threadModel?.title)
    }

    private fun createActivityNotif(item: ActivityNotification): String {
        notificationImage = item.user?.avatar?.image
        return context.getString(R.string.s_space_s)
            .format(item.user?.name, item.context)
    }


    private fun createFollowingNotification(item: FollowingNotificationModel): String {
        notificationImage = item.userModel?.avatar?.image
        return context.getString(R.string.s_space_s)
            .format(item.userModel?.name, item.context)
    }

    private fun createMediaDataChangeNotification(item: MediaDataChangeNotificationModel): String {
        notificationImage = item.media?.coverImage?.image
        return context.getString(R.string.s_space_s)
            .format(item.media?.title?.title(context), item.context)
    }


    private fun createMediaMergeNotification(item: MediaMergeNotificationModel): String {
        notificationImage = item.media?.coverImage?.image
        return context.getString(R.string.s_space_s)
            .format(item.media?.title?.title(context), item.context)
    }

    private fun createMediaDeleteNotification(item: MediaDeletionNotificationModel): String {
        return context.getString(R.string.s_space_s)
            .format(item.deletedMediaTitle, item.context)
    }

    private fun createNotificationPendingIntent() {
        val intent = Intent(Intent.ACTION_VIEW, null, context, MainActivity::class.java).also {
            it.putExtra(
                LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                LauncherShortcuts.NOTIFICATION.ordinal
            )
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                getPendingIntentUpdateFlag()
            )
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val defaultChan =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        defaultChan.enableVibration(true)
        defaultChan.vibrationPattern = longArrayOf(1000) /* ms */
        defaultChan.enableLights(true)
        defaultChan.lightColor = DynamicTheme.getInstance().get().accentColor
        notificationManagerCompat.createNotificationChannel(defaultChan)
    }

    private fun isNewNotification(item: NotificationModel?): Boolean {
        if (getLastNotification(context) == -1) {
            setNewNotification(item)
            return false
        }
        return getLastNotification(context) != item?.id
    }

    private fun setNewNotification(item: NotificationModel?) {
        setNewNotification(context, item?.id ?: -1)
    }


    override fun onStopped() {
        compositeDisposable.clear()
        super.onStopped()
    }

    private fun getNotificationPreference(key: String): Boolean {
        return context.getBoolean(key, true)
    }
}