package com.revolgenx.anilib.notification.ui.model

import com.revolgenx.anilib.NotificationQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.NotificationMediaContent
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.social.ui.model.ActivityUnionModel
import com.revolgenx.anilib.social.ui.model.ListActivityModel
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.thread.ui.model.ThreadModel
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel

interface BaseNotificationModel {
    val id: Int
    val createdAt: Int?
    val createdAtPrettyTime: String?
    val type: NotificationType?
    var unreadNotificationCount: Int
}

abstract class NotificationModel(id: Int) : BaseNotificationModel, BaseModel(id)

fun NotificationQuery.Notification.toModel(): NotificationModel? {
    return when {
        onAiringNotification != null -> {
            onAiringNotification.let {
                AiringNotificationModel(
                    id = it.id,
                    type = it.type,
                    episode = it.episode,
                    contexts = it.contexts?.filterNotNull(),
                    animeId = it.animeId,
                    media = it.media?.onMedia?.notificationMediaContent?.let { media ->
                        MediaModel(
                            id = media.id,
                            title = media.title?.mediaTitle?.toModel(),
                            coverImage = media.coverImage?.mediaCoverImage?.toModel(),
                            bannerImage = media.bannerImage,
                            format = media.format,
                            isAdult = media.isAdult == true,
                            type = media.type
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onFollowingNotification != null -> {
            onFollowingNotification.let {
                FollowingNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime(),
                )
            }
        }

        onActivityMessageNotification != null -> {
            onActivityMessageNotification.let {
                ActivityNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime(),
                    activityId = it.activityId,
                    context = it.context
                )
            }
        }

        onActivityMentionNotification != null -> {
            onActivityMentionNotification.let {
                ActivityNotificationModel(
                    id = it.id,
                    type = it.type,
                    activityId = it.activityId,
                    context = it.context,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },

                    activity = it.activity?.notificationActivity?.let {
                        ActivityUnionModel(
                            textActivityModel = it.onTextActivity?.let {
                                TextActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },

                            listActivityModel = it.onListActivity?.let {
                                ListActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },
                            messageActivityModel = it.onMessageActivity?.let {
                                MessageActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            }
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onActivityReplyNotification != null -> {
            onActivityReplyNotification.let {
                ActivityNotificationModel(
                    id = it.id,
                    type = it.type,
                    activityId = it.activityId,
                    context = it.context,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },

                    activity = it.activity?.notificationActivity?.let {
                        ActivityUnionModel(
                            textActivityModel = it.onTextActivity?.let {
                                TextActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },

                            listActivityModel = it.onListActivity?.let {
                                ListActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },
                            messageActivityModel = it.onMessageActivity?.let {
                                MessageActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            }
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onActivityReplySubscribedNotification != null -> {
            onActivityReplySubscribedNotification.let {
                ActivityNotificationModel(
                    id = it.id,
                    type = it.type,
                    activityId = it.activityId,
                    context = it.context,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },

                    activity = it.activity?.notificationActivity?.let {
                        ActivityUnionModel(
                            textActivityModel = it.onTextActivity?.let {
                                TextActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },

                            listActivityModel = it.onListActivity?.let {
                                ListActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },
                            messageActivityModel = it.onMessageActivity?.let {
                                MessageActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            }
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onActivityLikeNotification != null -> {
            onActivityLikeNotification.let {
                ActivityNotificationModel(
                    id = it.id,
                    type = it.type,
                    activityId = it.activityId,
                    context = it.context,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },

                    activity = it.activity?.notificationActivity?.let {
                        ActivityUnionModel(
                            textActivityModel = it.onTextActivity?.let {
                                TextActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },

                            listActivityModel = it.onListActivity?.let {
                                ListActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },
                            messageActivityModel = it.onMessageActivity?.let {
                                MessageActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            }
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onActivityReplyLikeNotification != null -> {
            onActivityReplyLikeNotification.let {
                ActivityNotificationModel(
                    id = it.id,
                    type = it.type,
                    activityId = it.activityId,
                    context = it.context,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let { user ->
                        UserModel(
                            id = user.id,
                            name = user.name,
                            avatar = user.avatar?.userAvatar?.toModel(),
                            bannerImage = user.bannerImage
                        )
                    },

                    activity = it.activity?.notificationActivity?.let {
                        ActivityUnionModel(
                            textActivityModel = it.onTextActivity?.let {
                                TextActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },

                            listActivityModel = it.onListActivity?.let {
                                ListActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            },
                            messageActivityModel = it.onMessageActivity?.let {
                                MessageActivityModel(
                                    id = it.id,
                                    siteUrl = it.siteUrl
                                )
                            }
                        )
                    },
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onThreadCommentMentionNotification != null -> {
            onThreadCommentMentionNotification.let {
                ThreadNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let {
                        UserModel(
                            id = it.id,
                            name = it.name,
                            avatar = it.avatar?.userAvatar?.toModel(),
                            bannerImage = it.bannerImage,
                        )
                    },
                    thread = it.thread?.onThread?.basicThreadContent?.let {
                        ThreadModel(
                            id = it.id,
                            title = it.title,
                            siteUrl = it.siteUrl
                        )
                    },
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onThreadCommentReplyNotification != null -> {
            onThreadCommentReplyNotification.let {
                ThreadNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let {
                        UserModel(
                            id = it.id,
                            name = it.name,
                            avatar = it.avatar?.userAvatar?.toModel(),
                            bannerImage = it.bannerImage,
                        )
                    },
                    thread = it.thread?.onThread?.basicThreadContent?.let {
                        ThreadModel(
                            id = it.id,
                            title = it.title,
                            siteUrl = it.siteUrl
                        )
                    },
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onThreadCommentSubscribedNotification != null -> {
            onThreadCommentSubscribedNotification.let {
                ThreadNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let {
                        UserModel(
                            id = it.id,
                            name = it.name,
                            avatar = it.avatar?.userAvatar?.toModel(),
                            bannerImage = it.bannerImage,
                        )
                    },
                    thread = it.thread?.onThread?.basicThreadContent?.let {
                        ThreadModel(
                            id = it.id,
                            title = it.title,
                            siteUrl = it.siteUrl
                        )
                    },
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onThreadCommentLikeNotification != null -> {
            onThreadCommentLikeNotification.let {
                ThreadNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let {
                        UserModel(
                            id = it.id,
                            name = it.name,
                            avatar = it.avatar?.userAvatar?.toModel(),
                            bannerImage = it.bannerImage,
                        )
                    },
                    thread = it.thread?.onThread?.basicThreadContent?.let {
                        ThreadModel(
                            id = it.id,
                            title = it.title,
                            siteUrl = it.siteUrl
                        )
                    },
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onThreadLikeNotification != null -> {
            onThreadLikeNotification.let {
                ThreadNotificationModel(
                    id = it.id,
                    type = it.type,
                    userId = it.userId,
                    user = it.user?.onUser?.notificationUserContent?.let {
                        UserModel(
                            id = it.id,
                            name = it.name,
                            avatar = it.avatar?.userAvatar?.toModel(),
                            bannerImage = it.bannerImage,
                        )
                    },
                    thread = it.thread?.onThread?.basicThreadContent?.let {
                        ThreadModel(
                            id = it.id,
                            title = it.title,
                            siteUrl = it.siteUrl
                        )
                    },
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onRelatedMediaAdditionNotification != null -> {
            onRelatedMediaAdditionNotification.let {
                RelatedMediaNotificationModel(
                    id = it.id,
                    type = it.type,
                    mediaId = it.mediaId,
                    media = it.media?.onMedia?.notificationMediaContent?.toModel(),
                    context = it.context,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onMediaDataChangeNotification != null -> {
            onMediaDataChangeNotification.let {
                MediaDataChangeNotificationModel(
                    id = it.id,
                    type = it.type,
                    context = it.context,
                    mediaId = it.mediaId,
                    media = it.media?.onMedia?.notificationMediaContent?.toModel(),
                    reason = it.reason,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onMediaMergeNotification != null -> {
            onMediaMergeNotification.let {
                MediaMergeNotificationModel(
                    id = it.id,
                    type = it.type,
                    context = it.context,
                    mediaId = it.mediaId,
                    media = it.media?.onMedia?.notificationMediaContent?.toModel(),
                    reason = it.reason,
                    deletedMediaTitles = it.deletedMediaTitles?.filterNotNull(),
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        onMediaDeletionNotification != null -> {
            onMediaDeletionNotification.let {
                MediaDeletionNotificationModel(
                    id = it.id,
                    type = it.type,
                    context = it.context,
                    reason = it.reason,
                    deletedMediaTitle = it.deletedMediaTitle,
                    createdAt = it.createdAt,
                    createdAtPrettyTime = it.createdAt?.toLong()?.prettyTime()
                )
            }
        }

        else -> {
            null
        }
    }
}


fun NotificationMediaContent.toModel() =
    MediaModel(
        id = id,
        title = title?.mediaTitle?.toModel(),
        coverImage = coverImage?.mediaCoverImage?.toModel(),
        bannerImage = bannerImage,
        format = format,
        isAdult = isAdult == true,
        type = type
    )
