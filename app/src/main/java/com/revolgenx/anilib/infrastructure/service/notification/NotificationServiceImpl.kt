package com.revolgenx.anilib.infrastructure.service.notification

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.constant.LIST_ACTIVITY
import com.revolgenx.anilib.constant.MESSAGE_ACTIVITY
import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.constant.TEXT_ACTIVITY
import com.revolgenx.anilib.data.field.notification.NotificationField
import com.revolgenx.anilib.data.field.notification.UserNotificationMutateField
import com.revolgenx.anilib.data.field.notification.UserNotificationSettingField
import com.revolgenx.anilib.data.model.user.UserPrefModel
import com.revolgenx.anilib.data.model.user.AvatarModel
import com.revolgenx.anilib.data.model.activity.ListActivityModel
import com.revolgenx.anilib.data.model.activity.MessageActivityModel
import com.revolgenx.anilib.data.model.activity.TextActivityModel
import com.revolgenx.anilib.data.model.notification.EmptyNotificationModel
import com.revolgenx.anilib.data.model.notification.FollowingNotificationModel
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.data.model.notification.activity.*
import com.revolgenx.anilib.data.model.notification.thread.*
import com.revolgenx.anilib.data.model.thread.ThreadCommentModel
import com.revolgenx.anilib.data.model.thread.ThreadModel
import com.revolgenx.anilib.fragment.NotificationActivity
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toBasicMediaContent
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.type.NotificationType
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
                                id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.FOLLOWING
                                userModel = it.user()?.fragments()?.notificationUserContent()?.let {
                                    UserPrefModel().also { user ->
                                        user.id = it.id()
                                        user.name = it.name()
                                        user.avatar = it.avatar()?.let {
                                            AvatarModel().also { img ->
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_MESSAGE
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                messageActivityModel = it.message()?.let {
                                    MessageActivityModel().also { msg ->
                                        msg.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_MENTION
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }

                                when (it.activity()?.__typename()) {
                                    TEXT_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
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
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
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
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_LIKE
                                activityId = it.activityId()
                                context = it.context()
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
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
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY_LIKE
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
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
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsTextActivity).let {
                                            textActivityModel = TextActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    LIST_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsListActivity).let {
                                            listActivityModel = ListActivityModel().also { model ->
                                                model.id = it.id()
                                                model.siteUrl = it.siteUrl()
                                            }
                                        }
                                    }
                                    MESSAGE_ACTIVITY -> {
                                        (it.activity()?.fragments()
                                            ?.notificationActivity() as NotificationActivity.AsMessageActivity).let {
                                            messageActivityModel =
                                                MessageActivityModel().also { model ->
                                                    model.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_COMMENT_MENTION
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        id = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_COMMENT_REPLY
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        id = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_SUBSCRIBED
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        id = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_COMMENT_LIKE
                                createdAt = it.createdAt()?.toLong()?.prettyTime()

                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        id = it.id()
                                        title = it.title()
                                        siteUrl = it.siteUrl()

                                    }
                                }
                                threadCommentModel = it.comment()?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id()
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
                                id = it.id()
                                type = it.type()?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_LIKE
                                createdAt = it.createdAt()?.toLong()?.prettyTime()
                                userPrefModel =
                                    it.user()?.fragments()?.notificationUserContent()?.let {
                                        UserPrefModel().also { user ->
                                            user.id = it.id()
                                            user.name = it.name()
                                            user.avatar = it.avatar()?.let {
                                                AvatarModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                }
                                            }
                                            user.bannerImage = it.bannerImage()
                                        }
                                    }
                                threadModel = it.thread()?.fragments()?.basicThreadContent()?.let {
                                    ThreadModel().apply {
                                        id = it.id()
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
                                id = it.id()
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


    override fun saveNotificationSettings(
        field: UserNotificationMutateField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(true))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }

    override fun getNotificationSettings(
        field: UserNotificationSettingField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Map<NotificationType, Boolean>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.User()?.options()?.notificationOptions()
                    ?.associateBy({ it.type()!! }, { it.enabled() == true })
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}