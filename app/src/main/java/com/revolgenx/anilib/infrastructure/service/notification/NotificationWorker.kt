package com.revolgenx.anilib.infrastructure.service.notification

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
import androidx.core.os.bundleOf
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
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.activity.ToolbarContainerActivity
import com.revolgenx.anilib.activity.UserProfileActivity
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.data.field.notification.NotificationField
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.ui.fragment.notification.NotificationFragment
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.data.model.notification.activity.*
import com.revolgenx.anilib.data.model.notification.thread.*
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class NotificationWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params),
    KoinComponent {

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
    private var notificationText: String? = null
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
            R.drawable.ic_notifications
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
            NotificationUnionType.ACTIVITY_MESSAGE -> {
                if(!getNotificationPreference(context.getString(R.string.activity_message_key))) return ""
                val activity = item as ActivityMessageNotification
                //createActivityPendingLink(activity)
                createActivityNotif(activity)
            }
            NotificationUnionType.ACTIVITY_REPLY -> {
                if(!getNotificationPreference(context.getString(R.string.activity_reply_mention_key))) return ""

                val activity = item as ActivityReplyNotification
                //createActivityPendingLink(activity)
                createActivityNotif(activity)
            }
            NotificationUnionType.ACTIVITY_MENTION -> {
                if(!getNotificationPreference(context.getString(R.string.activity_reply_mention_key))) return ""

                val activity = item as ActivityMentionNotification
                //createActivityPendingLink(activity)
                createActivityNotif(activity)
            }
            NotificationUnionType.ACTIVITY_LIKE -> {
                if(!getNotificationPreference(context.getString(R.string.activity_like_key))) return ""

                val activity = item as ActivityLikeNotification
                //createActivityPendingLink(activity)
                createActivityNotif(activity)
            }
            NotificationUnionType.ACTIVITY_REPLY_LIKE -> {
                if(!getNotificationPreference(context.getString(R.string.activity_reply_like_key))) return ""

                val activity = item as ActivityReplyLikeNotification
                //createActivityPendingLink(activity)
                createActivityNotif(activity)
            }
            NotificationUnionType.ACTIVITY_REPLY_SUBSCRIBED -> {
                if(!getNotificationPreference(context.getString(R.string.activity_reply_mention_key))) return ""

                val activity = item as ActivityReplySubscribedNotification
                //createActivityPendingLink(activity)
                createActivityNotif(activity)
            }

            NotificationUnionType.THREAD_COMMENT_MENTION -> {
                if(!getNotificationPreference(context.getString(R.string.thread_comment_mention_key))) return ""

                val thread = item as ThreadCommentMentionNotification
                //createThreadPendingLink(thread)
                createThreadNotif(thread)
            }
            NotificationUnionType.THREAD_SUBSCRIBED -> {
                //todo://check for notification not added in here
                val thread = item as ThreadCommentSubscribedNotification
                //createThreadPendingLink(thread)
                createThreadNotif(thread)
            }
            NotificationUnionType.THREAD_COMMENT_REPLY -> {
                if(!getNotificationPreference(context.getString(R.string.thread_comment_reply_key))) return ""

                val thread = item as ThreadCommentReplyNotification
                //createThreadPendingLink(thread)
                createThreadNotif(thread)
            }
            NotificationUnionType.THREAD_LIKE -> {
                if(!getNotificationPreference(context.getString(R.string.thread_like_key))) return ""

                val thread = item as ThreadLikeNotification
                //createThreadPendingLink(thread)
                createThreadNotif(thread)
            }
            NotificationUnionType.THREAD_COMMENT_LIKE -> {
                if(!getNotificationPreference(context.getString(R.string.thread_comment_like_key))) return ""

                val thread = item as ThreadCommentLikeNotification
                //createThreadPendingLink(thread)
                createThreadNotif(thread)
            }
            NotificationUnionType.AIRING -> {
                if(!getNotificationPreference(context.getString(R.string.airing_notification_key))) return ""

                (item as AiringNotificationModel).let {
                    //                    createMediaPendingIntent(it)
                    notificationImage = it.commonMediaModel?.coverImage?.image(context)
                    String.format(
                        Locale.getDefault(),
                        context.getString(R.string.episode_airing_notif)
                        ,
                        it.contexts!![0],
                        it.episode,
                        it.contexts!![1],
                        it.commonMediaModel?.title?.title(context),
                        it.contexts!![2]
                    )
                }
            }

            NotificationUnionType.FOLLOWING -> {
                if(!getNotificationPreference(context.getString(R.string.following_key))) return ""

//                createUserPendingIntent(item as ActivityNotification)
                createActivityNotif(item as ActivityNotification)
            }
            NotificationUnionType.RELATED_MEDIA_ADDITION -> {
                if(!getNotificationPreference(context.getString(R.string.related_media_added_notif))) return ""

                (item as RelatedMediaNotificationModel)
                notificationImage = item.commonMediaModel?.coverImage?.image
                return context.getString(R.string.s_space_s)
                    .format(item.commonMediaModel?.title?.title(context), item.context)
            }

            else -> ""
        }

    }

    private fun createThreadNotif(item: ThreadNotification): String {
        notificationImage = item.userPrefModel?.avatar?.image
        return context.getString(R.string.thread_notif_s)
            .format(item.userPrefModel?.userName, item.context, item.threadModel?.title)
    }

    private fun createActivityNotif(item: ActivityNotification): String {
        notificationImage = item.userPrefModel?.avatar?.image
        return context.getString(R.string.s_space_s)
            .format(item.userPrefModel?.userName, item.context)
    }

    private fun createThreadPendingLink(thread: ThreadNotification) {
        thread.threadModel?.siteUrl?.let {
            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(Intent.ACTION_VIEW, it.trim().toUri()),
                PendingIntent.FLAG_ONE_SHOT
            )
        }
    }


    private fun createNotificationPendingIntent() {
        val intent = Intent(context, ToolbarContainerActivity::class.java).also {
            it.putExtra(
                ToolbarContainerActivity.toolbarFragmentContainerKey, ParcelableFragment(
                    NotificationFragment::class.java,
                    bundleOf(
                        UserMeta.userMetaKey to UserMeta(
                            context.userId(),
                            null,
                            true
                        )
                    )
                )
            )
        }
        pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createActivityPendingLink(activity: ActivityNotification) {
        var siteUrl: String? = null
        activity.textActivityModel?.let {
            siteUrl = it.siteUrl
        }
        activity.listActivityModel?.let {
            siteUrl = it.siteUrl
        }

        activity.messageActivityModel?.let {
            siteUrl = it.siteUrl
        }

        siteUrl?.let {
            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(Intent.ACTION_VIEW, it.trim().toUri()),
                PendingIntent.FLAG_ONE_SHOT
            )
        }
    }

    private fun createMediaPendingIntent(item: AiringNotificationModel) {
        val intent = Intent(context, MediaBrowseActivity::class.java).apply {
            item.commonMediaModel?.let { data ->
                this.putExtra(
                    MediaBrowseActivity.MEDIA_BROWSER_META, MediaBrowserMeta(
                        data.mediaId,
                        data.type!!,
                        data.title!!.romaji!!,
                        data.coverImage!!.image(context),
                        data.coverImage!!.largeImage,
                        data.bannerImage
                    )
                )
            }
        }
        pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createUserPendingIntent(item: ActivityNotification) {
        val intent = Intent(context, UserProfileActivity::class.java).also {
            it.putExtra(
                UserProfileActivity.USER_ACTIVITY_META_KEY,
                UserMeta(item.userPrefModel?.userId, null)
            )
        }
        pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
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
        return getLastNotification(context) != item?.baseId
    }

    private fun setNewNotification(item: NotificationModel?) {
        setNewNotification(context, item?.baseId ?: -1)
    }


    override fun onStopped() {
        compositeDisposable.clear()
        super.onStopped()
    }

    private fun getNotificationPreference(key: String):Boolean {
        return context.getBoolean(key, true)
    }
}