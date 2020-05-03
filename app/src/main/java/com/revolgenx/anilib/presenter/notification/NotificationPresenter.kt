package com.revolgenx.anilib.presenter.notification

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.UserBrowseEvent
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.model.notification.activity.ActivityMessageNotification
import com.revolgenx.anilib.model.notification.activity.ActivityNotification
import com.revolgenx.anilib.model.notification.activity.AiringNotificationModel
import com.revolgenx.anilib.model.notification.thread.ThreadNotification
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
                    }
                }
                NotificationUnionType.ACTIVITY_REPLY -> {
                    createActivityNotif(item as ActivityNotification)

                }
                NotificationUnionType.FOLLOWING -> {
                    createActivityNotif(item as ActivityNotification)

                    setOnClickListener {
                        UserBrowseEvent(item.userModel?.userId).postEvent
                    }
                }
                NotificationUnionType.ACTIVITY_MENTION -> {
                    createActivityNotif(item as ActivityNotification)

                }
                NotificationUnionType.THREAD_COMMENT_MENTION -> {
                    createThreadNotif(item as ThreadNotification)

                }
                NotificationUnionType.THREAD_SUBSCRIBED -> {
                    createThreadNotif(item as ThreadNotification)

                }
                NotificationUnionType.THREAD_COMMENT_REPLY -> {
                    createThreadNotif(item as ThreadNotification)
                }
                NotificationUnionType.AIRING -> {
                    (item as AiringNotificationModel).let {
                        val roundingParams = RoundingParams.fromCornersRadius(6f)
                        notificationMediaDrawee.hierarchy = GenericDraweeHierarchyBuilder(resources)
                            .setRoundingParams(roundingParams)
                            .build()
                        notificationMediaDrawee.setImageURI(it.commonMediaModel?.coverImage?.image)
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
                                    it.commonMediaModel?.coverImage!!.image,
                                    it.commonMediaModel?.coverImage!!.largeImage,
                                    it.commonMediaModel?.bannerImage
                                ), notificationMediaDrawee
                            ).postEvent
                        }
                    }
                }
                NotificationUnionType.ACTIVITY_LIKE -> {
                    createActivityNotif(item as ActivityNotification)
                }
                NotificationUnionType.ACTIVITY_REPLY_LIKE -> {
                    createActivityNotif(item as ActivityNotification)
                }
                NotificationUnionType.THREAD_LIKE -> {
                    createThreadNotif(item as ThreadNotification)
                }
                NotificationUnionType.THREAD_COMMENT_LIKE -> {
                    createThreadNotif(item as ThreadNotification)
                }
                NotificationUnionType.ACTIVITY_REPLY_SUBSCRIBED -> {
                    createActivityNotif(item as ActivityNotification)
                }
                NotificationUnionType.RELATED_MEDIA_ADDITION -> {

                }
            }
        }

        holder.itemView.apply {
            val roundingParams = RoundingParams.fromCornersRadius(7f)
            notificationMediaDrawee.hierarchy = GenericDraweeHierarchyBuilder(resources)
                .setRoundingParams(roundingParams)
                .build()
        }
    }

    private fun View.createActivityNotif(item: ActivityNotification) {
        val roundingParams = RoundingParams.asCircle()
        notificationMediaDrawee.hierarchy = GenericDraweeHierarchyBuilder(resources)
            .setRoundingParams(roundingParams)
            .build()
        notificationMediaDrawee.setImageURI(item.userModel?.avatar?.image)
        notificationTitleTv.text = context.getString(R.string.activity_notif_s)
            .format(item.userModel?.userName, item.context)
        notificationCreatedTv.text = item.createdAt
    }

    private fun View.createThreadNotif(item: ThreadNotification) {
        val roundingParams = RoundingParams.asCircle()
        notificationMediaDrawee.hierarchy = GenericDraweeHierarchyBuilder(resources)
            .setRoundingParams(roundingParams)
            .build()
        notificationMediaDrawee.setImageURI(item.userModel?.avatar?.image)
        notificationTitleTv.text = context.getString(R.string.thread_notif_s)
            .format(item.userModel?.userName, item.context, item.threadModel?.title)
        notificationCreatedTv.text = item.createdAt
    }
}
