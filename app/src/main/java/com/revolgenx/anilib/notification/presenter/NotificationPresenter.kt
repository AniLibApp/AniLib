package com.revolgenx.anilib.notification.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.NotificationPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenActivityInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.notification.data.model.FollowingNotificationModel
import com.revolgenx.anilib.notification.data.model.activity.*
import com.revolgenx.anilib.notification.data.model.media.MediaDataChangeNotificationModel
import com.revolgenx.anilib.notification.data.model.media.MediaDeletionNotificationModel
import com.revolgenx.anilib.notification.data.model.media.MediaMergeNotificationModel
import com.revolgenx.anilib.notification.data.model.media.RelatedMediaNotificationModel
import com.revolgenx.anilib.notification.data.model.thread.*
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.notification.data.model.NotificationModel
import com.revolgenx.anilib.util.openLink
import java.util.*

class NotificationPresenter(context: Context) :
    BasePresenter<NotificationPresenterLayoutBinding, NotificationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): NotificationPresenterLayoutBinding {
        return NotificationPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<NotificationModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()?.apply {
            notificationReasonTv.text = ""
            notificationReasonTv.setOnClickListener(null)
            when (item.notificationUnionType) {
                NotificationUnionType.ACTIVITY_MESSAGE -> {
                    val activity = item as ActivityMessageNotification
                    createActivityNotif(activity)
                    root.setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY -> {
                    val activity = item as ActivityReplyNotification
                    createActivityNotif(activity)
                    root.setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_MENTION -> {
                    val activity = item as ActivityMentionNotification
                    createActivityNotif(activity)
                    root.setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_LIKE -> {

                    val activity = item as ActivityLikeNotification
                    createActivityNotif(activity)
                    root.setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY_LIKE -> {

                    val activity = item as ActivityReplyLikeNotification
                    createActivityNotif(activity)
                    root.setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY_SUBSCRIBED -> {
                    val activity = item as ActivityReplySubscribedNotification
                    createActivityNotif(activity)
                    root.setOnClickListener {
                        openActivityLink(activity)
                    }
                }

                NotificationUnionType.THREAD_COMMENT_MENTION -> {
                    val thread = item as ThreadCommentMentionNotification
                    createThreadNotif(thread)
                    root.setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_SUBSCRIBED -> {
                    val thread = item as ThreadCommentSubscribedNotification
                    createThreadNotif(thread)
                    root.setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_COMMENT_REPLY -> {
                    val thread = item as ThreadCommentReplyNotification
                    createThreadNotif(thread)
                    root.setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_LIKE -> {
                    val thread = item as ThreadLikeNotification
                    createThreadNotif(thread)
                    root.setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_COMMENT_LIKE -> {
                    val thread = item as ThreadCommentLikeNotification
                    createThreadNotif(thread)
                    root.setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.AIRING -> {
                    (item as AiringNotificationModel).let {
                        notificationMediaDrawee.setImageURI(
                            it.media?.coverImage?.image()
                        )
                        notificationCreatedTv.text = it.createdAt
                        notificationTitleTv.text = String.format(
                            Locale.getDefault(),
                            context.getString(R.string.episode_airing_notif),
                            it.contexts!![0],
                            it.episode,
                            it.contexts!![1],
                            it.media?.title?.title(),
                            it.contexts!![2]
                        )

                        root.setOnClickListener { _ ->
                            OpenMediaInfoEvent(
                                MediaInfoMeta(
                                    it.media?.id,
                                    it.media?.type!!,
                                    it.media?.title!!.romaji!!,
                                    it.media?.coverImage!!.image(),
                                    it.media?.coverImage!!.largeImage,
                                    it.media?.bannerImage
                                )
                            ).postEvent
                        }
                    }
                }

                NotificationUnionType.FOLLOWING -> {
                    (item as FollowingNotificationModel)

                    notificationMediaDrawee.setImageURI(item.userModel?.avatar?.image)
                    notificationTitleTv.text = context.getString(R.string.s_space_s)
                        .format(item.userModel?.name, item.context)
                    notificationCreatedTv.text = item.createdAt

                    root.setOnClickListener {
                        OpenUserProfileEvent(item.userModel?.id).postEvent
                    }
                }
                NotificationUnionType.RELATED_MEDIA_ADDITION -> {
                    (item as RelatedMediaNotificationModel).let {
                        notificationMediaDrawee.setImageURI(
                            it.media?.coverImage?.image()
                        )
                        notificationCreatedTv.text = it.createdAt
                        notificationTitleTv.text =
                            context.getString(R.string.s_space_s).format(
                                it.media?.title?.title(), it.context
                            )

                        root.setOnClickListener { _ ->
                            OpenMediaInfoEvent(
                                MediaInfoMeta(
                                    it.media?.id,
                                    it.media?.type,
                                    it.media?.title!!.romaji!!,
                                    it.media?.coverImage!!.image(),
                                    it.media?.coverImage!!.largeImage,
                                    it.media?.bannerImage
                                )
                            ).postEvent

                        }
                    }
                }
                NotificationUnionType.MEDIA_DATA_CHANGE -> {
                    (item as MediaDataChangeNotificationModel).let {
                        root.setOnClickListener { _ ->
                            OpenMediaInfoEvent(
                                MediaInfoMeta(
                                    it.media?.id,
                                    it.media?.type,
                                    it.media?.title!!.romaji!!,
                                    it.media?.coverImage!!.image(),
                                    it.media?.coverImage!!.largeImage,
                                    it.media?.bannerImage
                                )
                            ).postEvent
                        }

                        notificationMediaDrawee.setImageURI(
                            it.media?.coverImage?.image()
                        )

                        notificationCreatedTv.text = it.createdAt
                        notificationTitleTv.text =
                            context.getString(R.string.s_space_s).format(
                                it.media?.title?.title(), it.context
                            )
                        notificationReasonTv.setText(R.string.show_reason)
                        notificationReasonTv.setOnClickListener { _ ->
                            notificationReasonTv.text = it.reason
                        }
                    }
                }
                NotificationUnionType.MEDIA_MERGE -> {
                    (item as MediaMergeNotificationModel).let {
                        root.setOnClickListener { _ ->
                            OpenMediaInfoEvent(
                                MediaInfoMeta(
                                    it.media?.id,
                                    it.media?.type,
                                    it.media?.title!!.romaji!!,
                                    it.media?.coverImage!!.image(),
                                    it.media?.coverImage!!.largeImage,
                                    it.media?.bannerImage
                                )
                            ).postEvent
                        }

                        notificationMediaDrawee.setImageURI(
                            it.media?.coverImage?.image()
                        )
                        notificationCreatedTv.text = it.createdAt
                        notificationTitleTv.text =
                            context.getString(R.string.s_space_s).format(
                                it.media?.title?.title(), it.context
                            )
                        notificationReasonTv.setText(R.string.show_reason)
                        notificationReasonTv.setOnClickListener { _ ->
                            notificationReasonTv.text = it.reason
                        }
                    }
                }
                NotificationUnionType.MEDIA_DELETION -> {
                    (item as MediaDeletionNotificationModel).let {
                        notificationMediaDrawee.setActualImageResource(R.drawable.ic_delete)
                        notificationTitleTv.text = context.getString(R.string.s_space_s)
                            .format(it.deletedMediaTitle, it.context)
                        notificationCreatedTv.text = it.createdAt
                        notificationReasonTv.setText(R.string.show_reason)
                        notificationReasonTv.setOnClickListener { _ ->
                            notificationReasonTv.text = it.reason
                        }
                    }
                }

                else -> {}
            }
        }

    }

    private fun openThreadLink(thread: ThreadNotification) {
        thread.threadModel?.siteUrl?.let {
            context.openLink(it)
        }
    }

    private fun openActivityLink(activity: ActivityNotification) {
        OpenActivityInfoEvent(activity.activityId ?: -1).postEvent
    }

    private fun NotificationPresenterLayoutBinding.createActivityNotif(item: ActivityNotification) {
        notificationMediaDrawee.setImageURI(item.user?.avatar?.image)
        notificationTitleTv.text = context.getString(R.string.s_space_s)
            .format(item.user?.name, item.context)
        notificationCreatedTv.text = item.createdAt
    }

    private fun NotificationPresenterLayoutBinding.createThreadNotif(item: ThreadNotification) {
        notificationMediaDrawee.setImageURI(item.user?.avatar?.image)
        notificationTitleTv.text = context.getString(R.string.thread_notif_s)
            .format(item.user?.name, item.context, item.threadModel?.title)
        notificationCreatedTv.text = item.createdAt
    }
}