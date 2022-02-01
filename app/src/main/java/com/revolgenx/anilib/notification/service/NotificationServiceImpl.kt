package com.revolgenx.anilib.notification.service

import com.revolgenx.anilib.constant.NotificationUnionType
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.field.UserNotificationMutateField
import com.revolgenx.anilib.notification.data.field.UserNotificationSettingField
import com.revolgenx.anilib.notification.data.model.*
import com.revolgenx.anilib.notification.data.model.activity.*
import com.revolgenx.anilib.notification.data.model.media.*
import com.revolgenx.anilib.notification.data.model.thread.*
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.data.model.toModel
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
            response.data?.page?.notifications?.filterNotNull()?.map {
                when {
                    it.onAiringNotification != null -> {
                        it.onAiringNotification.let {
                            AiringNotificationModel().apply {
                                id = it.id
                                type = it.type?.ordinal


                                notificationUnionType = NotificationUnionType.AIRING
                                episode = it.episode
                                contexts = it.contexts?.filterNotNull()
                                media = it.media?.onMedia?.notificationMediaContent?.let { media ->
                                    MediaModel().also { m ->
                                        m.id = media.id
                                        m.title = media.title?.mediaTitle?.toModel()
                                        m.coverImage = media.coverImage?.mediaCoverImage?.toModel()
                                        m.bannerImage = media.bannerImage
                                        m.format = media.format?.ordinal
                                        m.isAdult = media.isAdult == true
                                        m.type = media.type?.ordinal
                                    }
                                }
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    it.onFollowingNotification != null -> {
                        it.onFollowingNotification.let {
                            FollowingNotificationModel().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.FOLLOWING
                                userModel = it.user?.onUser?.notificationUserContent?.let {
                                    UserModel().also { user ->
                                        user.id = it.id
                                        user.name = it.name
                                        user.avatar = it.avatar?.userAvatar?.toModel()
                                        user.bannerImage = it.bannerImage
                                    }
                                }
                                context = it.context
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    it.onActivityMessageNotification != null -> {
                        it.onActivityMessageNotification.let {
                            ActivityMessageNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_MESSAGE
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                                activityId = it.activityId
                                context = it.context
                            }
                        }
                    }
                    it.onActivityMentionNotification != null -> {
                        it.onActivityMentionNotification.let {
                            ActivityMentionNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_MENTION
                                activityId = it.activityId
                                context = it.context
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }

                                textActivityModel =
                                    it.activity?.notificationActivity?.onTextActivity?.let {
                                        TextActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                listActivityModel =
                                    it.activity?.notificationActivity?.onListActivity?.let {
                                        ListActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }



                                messageActivityModel =
                                    it.activity?.notificationActivity?.onMessageActivity?.let {
                                        MessageActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    it.onActivityReplyNotification != null -> {
                        it.onActivityReplyNotification.let {
                            ActivityReplyNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_REPLY
                                activityId = it.activityId
                                context = it.context
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                createdAt = it.createdAt?.toLong()?.prettyTime()

                                textActivityModel =
                                    it.activity?.notificationActivity?.onTextActivity?.let {
                                        TextActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                listActivityModel =
                                    it.activity?.notificationActivity?.onListActivity?.let {
                                        ListActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                messageActivityModel =
                                    it.activity?.notificationActivity?.onMessageActivity?.let {
                                        MessageActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                            }
                        }
                    }
                    it.onActivityReplySubscribedNotification != null -> {
                        it.onActivityReplySubscribedNotification.let {
                            ActivityReplySubscribedNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType =
                                    NotificationUnionType.ACTIVITY_REPLY_SUBSCRIBED
                                activityId = it.activityId
                                context = it.context
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                createdAt = it.createdAt?.toLong()?.prettyTime()

                                textActivityModel =
                                    it.activity?.notificationActivity?.onTextActivity?.let {
                                        TextActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                listActivityModel =
                                    it.activity?.notificationActivity?.onListActivity?.let {
                                        ListActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                messageActivityModel =
                                    it.activity?.notificationActivity?.onMessageActivity?.let {
                                        MessageActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }
                            }

                        }
                    }
                    it.onActivityLikeNotification != null -> {
                        it.onActivityLikeNotification.let {

                            ActivityLikeNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.ACTIVITY_LIKE
                                activityId = it.activityId
                                context = it.context
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                createdAt = it.createdAt?.toLong()?.prettyTime()


                                textActivityModel =
                                    it.activity?.notificationActivity?.onTextActivity?.let {
                                        TextActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                listActivityModel =
                                    it.activity?.notificationActivity?.onListActivity?.let {
                                        ListActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                messageActivityModel =
                                    it.activity?.notificationActivity?.onMessageActivity?.let {
                                        MessageActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }
                            }
                        }
                    }
                    it.onActivityReplyLikeNotification != null -> {
                        it.onActivityReplyLikeNotification.let {
                            ActivityReplyLikeNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType =
                                    NotificationUnionType.ACTIVITY_REPLY_LIKE
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }

                                activityId = it.activityId
                                context = it.context
                                createdAt = it.createdAt?.toLong()?.prettyTime()

                                textActivityModel =
                                    it.activity?.notificationActivity?.onTextActivity?.let {
                                        TextActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                listActivityModel =
                                    it.activity?.notificationActivity?.onListActivity?.let {
                                        ListActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }

                                messageActivityModel =
                                    it.activity?.notificationActivity?.onMessageActivity?.let {
                                        MessageActivityModel().also { model ->
                                            model.id = it.id
                                            model.siteUrl = it.siteUrl
                                        }
                                    }
                            }
                        }
                    }
                    it.onThreadCommentMentionNotification != null -> {
                        it.onThreadCommentMentionNotification.let {
                            ThreadCommentMentionNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType =
                                    NotificationUnionType.THREAD_COMMENT_MENTION
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                threadModel =
                                    it.thread?.onThread?.basicThreadContent?.let {
                                        ThreadModel().apply {
                                            id = it.id
                                            title = it.title
                                            siteUrl = it.siteUrl

                                        }
                                    }
                                threadCommentModel = it.comment?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id
                                        cModel.siteUrl = it.siteUrl
                                    }

                                }
                                context = it.context
                                createdAt = it.createdAt?.toLong()?.prettyTime()


                            }
                        }
                    }
                    it.onThreadCommentReplyNotification != null -> {
                        it.onThreadCommentReplyNotification.let {
                            ThreadCommentReplyNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType =
                                    NotificationUnionType.THREAD_COMMENT_REPLY
                                createdAt = it.createdAt?.toLong()?.prettyTime()

                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                threadModel =
                                    it.thread?.onThread?.basicThreadContent?.let {
                                        ThreadModel().apply {
                                            id = it.id
                                            title = it.title
                                            siteUrl = it.siteUrl

                                        }
                                    }
                                threadCommentModel = it.comment?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id
                                        cModel.siteUrl = it.siteUrl
                                    }

                                }
                                context = it.context
                            }
                        }
                    }
                    it.onThreadCommentSubscribedNotification != null -> {
                        it.onThreadCommentSubscribedNotification.let {
                            ThreadCommentSubscribedNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_SUBSCRIBED
                                createdAt = it.createdAt?.toLong()?.prettyTime()

                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                threadModel =
                                    it.thread?.onThread?.basicThreadContent?.let {
                                        ThreadModel().apply {
                                            id = it.id
                                            title = it.title
                                            siteUrl = it.siteUrl

                                        }
                                    }
                                threadCommentModel = it.comment?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id
                                        cModel.siteUrl = it.siteUrl
                                    }

                                }
                                context = it.context
                            }
                        }
                    }
                    it.onThreadCommentLikeNotification != null -> {
                        it.onThreadCommentLikeNotification.let {
                            ThreadCommentLikeNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType =
                                    NotificationUnionType.THREAD_COMMENT_LIKE
                                createdAt = it.createdAt?.toLong()?.prettyTime()

                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                threadModel =
                                    it.thread?.onThread?.basicThreadContent?.let {
                                        ThreadModel().apply {
                                            id = it.id
                                            title = it.title
                                            siteUrl = it.siteUrl

                                        }
                                    }
                                threadCommentModel = it.comment?.let {
                                    ThreadCommentModel().also { cModel ->
                                        cModel.id = it.id
                                        cModel.siteUrl = it.siteUrl
                                    }

                                }
                                context = it.context
                            }
                        }
                    }
                    it.onThreadLikeNotification != null -> {
                        it.onThreadLikeNotification.let {
                            ThreadLikeNotification().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.THREAD_LIKE
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                                user =
                                    it.user?.onUser?.notificationUserContent?.let {
                                        UserModel().also { user ->
                                            user.id = it.id
                                            user.name = it.name
                                            user.avatar = it.avatar?.userAvatar?.toModel()
                                            user.bannerImage = it.bannerImage
                                        }
                                    }
                                threadModel =
                                    it.thread?.onThread?.basicThreadContent?.let {
                                        ThreadModel().apply {
                                            id = it.id
                                            title = it.title
                                            siteUrl = it.siteUrl

                                        }
                                    }
                                context = it.context
                            }
                        }
                    }
                    it.onRelatedMediaAdditionNotification != null -> {
                        it.onRelatedMediaAdditionNotification.let {
                            RelatedMediaNotificationModel().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType =
                                    NotificationUnionType.RELATED_MEDIA_ADDITION
                                context = it.context
                                media = it.media?.onMedia?.notificationMediaContent?.toModel()
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    it.onMediaDataChangeNotification != null -> {
                        it.onMediaDataChangeNotification.let {
                            MediaDataChangeNotificationModel().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.MEDIA_DATA_CHANGE
                                context = it.context
                                media = it.media?.onMedia?.notificationMediaContent?.toModel()
                                reason = it.reason
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    it.onMediaMergeNotification != null -> {
                        it.onMediaMergeNotification.let {
                            MediaMergeNotificationModel().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.MEDIA_MERGE
                                context = it.context
                                media = it.media?.onMedia?.notificationMediaContent?.toModel()
                                reason = it.reason
                                deletedMediaTitles = it.deletedMediaTitles?.filterNotNull()
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    it.onMediaDeletionNotification != null -> {
                        it.onMediaDeletionNotification.let {
                            MediaDeletionNotificationModel().apply {
                                id = it.id
                                type = it.type?.ordinal
                                notificationUnionType = NotificationUnionType.MEDIA_DELETION
                                context = it.context
                                reason = it.reason
                                deletedMediaTitle = it.deletedMediaTitle
                                createdAt = it.createdAt?.toLong()?.prettyTime()
                            }
                        }
                    }
                    else -> {
                        EmptyNotificationModel()
                    }
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback.invoke(Resource.success(it))
                },
                {
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
                it.data?.user?.options?.notificationOptions?.filterNotNull()?.associateBy({ it.type!! }, { it.enabled == true })
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