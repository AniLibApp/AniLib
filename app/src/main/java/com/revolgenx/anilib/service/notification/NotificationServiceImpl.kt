package com.revolgenx.anilib.service.notification

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.constant.LIST_ACTIVITY
import com.revolgenx.anilib.constant.MESSAGE_ACTIVITY
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.constant.TEXT_ACTIVITY
import com.revolgenx.anilib.field.notification.NotificationField
import com.revolgenx.anilib.fragment.NotificationActivity
import com.revolgenx.anilib.model.UserPrefModel
import com.revolgenx.anilib.model.UserAvatarImageModel
import com.revolgenx.anilib.model.activity.ListActivityModel
import com.revolgenx.anilib.model.activity.MessageActivityModel
import com.revolgenx.anilib.model.activity.TextActivityModel
import com.revolgenx.anilib.model.notification.EmptyNotificationModel
import com.revolgenx.anilib.model.notification.FollowingNotificationModel
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.model.notification.activity.*
import com.revolgenx.anilib.model.notification.thread.*
import com.revolgenx.anilib.model.thread.ThreadCommentModel
import com.revolgenx.anilib.model.thread.ThreadModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.toBasicMediaContent
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.util.prettyTime
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


//todo check all notification
class NotificationServiceImpl(private val graphRepository: BaseGraphRepository) :
    NotificationService {
    override fun getNotifications(
        field: NotificationField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<NotificationModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation()).map { response ->
            response.data()?.Page()?.notifications()?.map {
                when (it.__typename()) {
                    "AiringNotification" -> {
                        (it as NotificationQuery.AsAiringNotification).let {
                            AiringNotificationModel().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal


                                notificationUnionType = NotificationUnionType.AIRING
                                episode = it.episode()
                                contexts = it.contexts()
                                commonMediaModel = it.media()?.fragments()?.basicMediaContent()
                                    ?.toBasicMediaContent()
                                createdAt = it.createdAt()?.toLong()?.prettyTime()
                            }
                        }
                    }

                    "FollowingNotification" -> {
                        (it as NotificationQuery.AsFollowingNotification).let {
                            FollowingNotificationModel().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.FOLLOWING
                                userModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }
                                context = it.context()
                                createdAt = it.createdAt()?.toLong()?.prettyTime()
                            }
                        }
                    }
                    "ActivityMessageNotification" -> {
                        (it as NotificationQuery.AsActivityMessageNotification).let {
                            ActivityMessageNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_MESSAGE
                                userPrefModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }
                                messageActivityModel = it.message()?.let {
                                    MessageActivityModel().also { msg ->
                                        msg.baseId = it.id()
                                        msg.message = it.message()
                                        msg.siteUrl = it.siteUrl()
                                    }
                                }
                                createdAt = it.createdAt()?.toLong()?.prettyTime()
                                activityId = it.activityId()
                                context = it.context()
                            }
                        }

                    }
                    "ActivityMentionNotification" -> {
                        (it as NotificationQuery.AsActivityMentionNotification).let {
                            ActivityMentionNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_MENTION
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }

                                when (it.activity()?.__typename()) {
                                    TEXT_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.baseId = it.id()
                                                    model.siteUrl = it.siteUrl()
                                                }
                                        }

                                    }
                                }
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                            }
                        }

                    }
                    "ActivityReplyNotification" -> {
                        (it as NotificationQuery.AsActivityReplyNotification).let {

                            ActivityReplyNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                when (it.activity()?.__typename()) {
                                    TEXT_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.baseId = it.id()
                                                    model.siteUrl = it.siteUrl()
                                                }
                                        }

                                    }
                                }
                            }

                        }
                    }
                    "ActivityReplySubscribedNotification" -> {
                        (it as NotificationQuery.AsActivityReplyNotification).let {

                            ActivityReplySubscribedNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                when (it.activity()?.__typename()) {
                                    TEXT_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.baseId = it.id()
                                                    model.siteUrl = it.siteUrl()
                                                }
                                        }

                                    }
                                }
                            }

                        }
                    }
                    "ActivityLikeNotification" -> {
                        (it as NotificationQuery.AsActivityLikeNotification).let {

                            ActivityLikeNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_LIKE
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                when (it.activity()?.__typename()) {
                                    TEXT_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.baseId = it.id()
                                                    model.siteUrl = it.siteUrl()
                                                }
                                        }

                                    }
                                }
                            }
                        }
                    }
                    "ActivityReplyLikeNotification" -> {
                        (it as NotificationQuery.AsActivityReplyLikeNotification).let {
                            ActivityReplyLikeNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY_LIKE
                                userPrefModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.userId = it.id()
                                        user.userName = it.name()
                                        user.avatar = it.avatar()?.let {
                                            UserAvatarImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                            }
                                        }
                                        user.bannerImage = it.bannerImage()
                                    }
                                }

                                activityId = it.activityId()
                                context = it.context()
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                when (it.activity()?.__typename()) {
                                    TEXT_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.baseId = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.baseId = it.id()
                                                    model.siteUrl = it.siteUrl()
                                                }
                                        }

                                    }
                                }
                            }
                        }
                    }

                    "ThreadCommentMentionNotification" -> {
                        (it as NotificationQuery.AsThreadCommentMentionNotification).let {
                            ThreadCommentMentionNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_COMMENT_MENTION
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.userId = it.id()
                                            user.userName = it.name()
                                            user.avatar = it.avatar()?.let {
                                                UserAvatarImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        baseId = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.baseId = it.id()
                                        cModel.siteUrl = it.siteUrl()
                                    }

                                }
                                context = it.context()
                                createdAt = it.createdAt()?.toLong()?.prettyTime()


                            }
                        }
                    }
                    "ThreadCommentReplyNotification" -> {
                        (it as NotificationQuery.AsThreadCommentReplyNotification).let {
                            ThreadCommentReplyNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_COMMENT_REPLY
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.userId = it.id()
                                            user.userName = it.name()
                                            user.avatar = it.avatar()?.let {
                                                UserAvatarImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        baseId = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.baseId = it.id()
                                        cModel.siteUrl = it.siteUrl()
                                    }

                                }
                                context = it.context()
                            }
                        }
                    }
                    "ThreadCommentSubscribedNotification" -> {
                        (it as NotificationQuery.AsThreadCommentSubscribedNotification).let {
                            ThreadCommentSubscribedNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_SUBSCRIBED
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.userId = it.id()
                                            user.userName = it.name()
                                            user.avatar = it.avatar()?.let {
                                                UserAvatarImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        baseId = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.baseId = it.id()
                                        cModel.siteUrl = it.siteUrl()
                                    }

                                }
                                context = it.context()
                            }
                        }
                    }
                    "ThreadCommentLikeNotification" -> {
                        (it as NotificationQuery.AsThreadCommentLikeNotification).let {
                            ThreadCommentLikeNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_COMMENT_LIKE
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.userId = it.id()
                                            user.userName = it.name()
                                            user.avatar = it.avatar()?.let {
                                                UserAvatarImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        baseId = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.baseId = it.id()
                                        cModel.siteUrl = it.siteUrl()
                                    }

                                }
                                context = it.context()
                            }
                        }
                    }
                    "ThreadLikeNotification" -> {
                        (it as NotificationQuery.AsThreadLikeNotification).let {
                            ThreadLikeNotification().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_LIKE
                                createdAt = it.createdAt()?.toLong()?.prettyTime()
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.userId = it.id()
                                            user.userName = it.name()
                                            user.avatar = it.avatar()?.let {
                                                UserAvatarImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        baseId = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                context = it.context()
                            }
                        }
                    }

                    "RelatedMediaAdditionNotification" -> {
                        (it as NotificationQuery.AsRelatedMediaAdditionNotification).let {
                            RelatedMediaNotificationModel().apply {
                                baseId = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.RELATED_MEDIA_ADDITION
                                context = it.context()
                                commonMediaModel = it.media()?.fragments()?.basicMediaContent()
                                    ?.toBasicMediaContent()
                                createdAt = it.createdAt()?.toLong()?.prettyTime()
                            }
                        }
                    }
                    else -> EmptyNotificationModel()
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}