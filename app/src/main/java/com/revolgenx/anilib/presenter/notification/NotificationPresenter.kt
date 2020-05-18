package com.revolgenx.anilib.presenter.notification

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.UserBrowseEvent
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.notification.FollowingNotificationModel
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.model.notification.activity.*
import com.revolgenx.anilib.model.notification.thread.*
import com.revolgenx.anilib.util.openLink
import kotlinx.android.synthetic.main.notification_presenter_layout.view.*
import java.util.*


class NotificationPresenter(context: Context) : Presenter<NotificationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(
                R.layout.notification_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<NotificationModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {
            when (item.notificationUnionType) {
                NotificationUnionType.ACTIVITY_MESSAGE -> {
                    val activity = item as ActivityMessageNotification
                    createActivityNotif(activity)
                    setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY -> {
                    val activity = item as ActivityReplyNotification
                    createActivityNotif(activity)
                    setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_MENTION -> {

                    val activity = item as ActivityMentionNotification
                    createActivityNotif(activity)
                    setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_LIKE -> {

                    val activity = item as ActivityLikeNotification
                    createActivityNotif(activity)
                    setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY_LIKE -> {

                    val activity = item as ActivityReplyLikeNotification
                    createActivityNotif(activity)
                    setOnClickListener {
                        openActivityLink(activity)
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY_SUBSCRIBED -> {
                    val activity = item as ActivityReplySubscribedNotification
                    createActivityNotif(activity)
                    setOnClickListener {
                        openActivityLink(activity)
                    }
                }

                NotificationUnionType.THREAD_COMMENT_MENTION -> {
                    val thread = item as ThreadCommentMentionNotification
                    createThreadNotif(thread)
                    setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_SUBSCRIBED -> {
                    val thread = item as ThreadCommentSubscribedNotification
                    createThreadNotif(thread)
                    setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_COMMENT_REPLY -> {
                    val thread = item as ThreadCommentReplyNotification
                    createThreadNotif(thread)
                    setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_LIKE -> {
                    val thread = item as ThreadLikeNotification
                    createThreadNotif(thread)
                    setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.THREAD_COMMENT_LIKE -> {
                    val thread = item as ThreadCommentLikeNotification
                    createThreadNotif(thread)
                    setOnClickListener {
                        openThreadLink(thread)
                    }
                }
                NotificationUnionType.AIRING -> {
                    (item as AiringNotificationModel).let {
                        notificationMediaDrawee.setImageURI(
                            it.commonMediaModel?.coverImage?.image(
                                context
                            )
                        )
                        notificationCreatedTv.text = it.createdAt
                        notificationTitleTv.text = String.format(
                            Locale.getDefault(),
                            context.getString(R.string.episode_airing_notif)
                            ,
                            it.contexts!![0],
                            it.episode,
                            it.contexts!![1],
                            it.commonMediaModel?.title?.title(context),
                            it.contexts!![2]
                        )

                        setOnClickListener { _ ->
                            BrowseMediaEvent(
                                MediaBrowserMeta(
                                    it.commonMediaModel?.mediaId,
                                    it.commonMediaModel?.type!!,
                                    it.commonMediaModel?.title!!.romaji!!,
                                    it.commonMediaModel?.coverImage!!.image(context),
                                    it.commonMediaModel?.coverImage!!.largeImage,
                                    it.commonMediaModel?.bannerImage
                                ), notificationMediaDrawee
                            ).postEvent
                        }
                    }
                }

                NotificationUnionType.FOLLOWING -> {
                    (item as FollowingNotificationModel)

                    notificationMediaDrawee.setImageURI(item.userModel?.avatar?.image)
                    notificationTitleTv.text = context.getString(R.string.s_space_s)
                        .format(item.userModel?.userName, item.context)
                    setOnClickListener {
                        UserBrowseEvent(item.userModel?.userId).postEvent
                    }
                }
                NotificationUnionType.RELATED_MEDIA_ADDITION -> {
                    (item as RelatedMediaNotificationModel).let {
                        notificationMediaDrawee.setImageURI(
                            it.commonMediaModel?.coverImage?.image(
                                context
                            )
                        )
                        notificationCreatedTv.text = it.createdAt
                        notificationTitleTv.text =
                            context.getString(R.string.s_space_s).format(
                                it.commonMediaModel?.title?.title(context), it.context
                            )

                        setOnClickListener { _ ->
                            BrowseMediaEvent(
                                MediaBrowserMeta(
                                    it.commonMediaModel?.mediaId,
                                    it.commonMediaModel?.type!!,
                                    it.commonMediaModel?.title!!.romaji!!,
                                    it.commonMediaModel?.coverImage!!.image(context),
                                    it.commonMediaModel?.coverImage!!.largeImage,
                                    it.commonMediaModel?.bannerImage
                                ), notificationMediaDrawee
                            ).postEvent

                        }
                    }
                }
            }
        }

    }

    private fun openThreadLink(thread: ThreadNotification) {
        thread.threadModel?.siteUrl?.let {
            context.openLink(it)
        }
    }

    private fun openActivityLink(activity: ActivityNotification) {
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
        context.openLink(siteUrl)
    }

    private fun View.createActivityNotif(item: ActivityNotification) {
        notificationMediaDrawee.setImageURI(item.userModel?.avatar?.image)
        notificationTitleTv.text = context.getString(R.string.s_space_s)
            .format(item.userModel?.userName, item.context)
        notificationCreatedTv.text = item.createdAt
    }

    private fun View.createThreadNotif(item: ThreadNotification) {
        notificationMediaDrawee.setImageURI(item.userModel?.avatar?.image)
        notificationTitleTv.text = context.getString(R.string.thread_notif_s)
            .format(item.userModel?.userName, item.context, item.threadModel?.title)
        notificationCreatedTv.text = item.createdAt
    }
}
