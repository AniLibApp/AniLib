package com.revolgenx.anilib.common.repository.network.converter

import com.revolgenx.anilib.*
import com.revolgenx.anilib.media.data.model.MediaTitleModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionTypeModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.app.setting.data.model.getRowOrder
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.user.data.model.UserAvatarModel
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.fragment.*
import com.revolgenx.anilib.media.data.model.*
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify
import com.revolgenx.anilib.util.prettyTime

fun BasicUserQuery.User.toBasicUserModel() = UserModel().also {
    it.id = id
    it.name = name
    it.unreadNotificationCount = unreadNotificationCount
    it.mediaListOptions = mediaListOptions?.userMediaListOptions?.toModel()
    it.options = options?.userMediaOptions?.toModel()
}


fun MediaWatchQuery.Media.toModel() = streamingEpisodes?.mapNotNull {ep->
    ep?.let {
        MediaStreamingEpisodeModel().also { model ->
            model.site = it.site
            model.thumbnail = it.thumbnail
            model.title = it.title
            model.url = it.url
        }
    }
}


fun UserMediaOptions.toModel() = UserOptionsModel(
    titleLanguage!!.ordinal,
    displayAdultContent!!,
    airingNotifications!!,
    profileColor
)

fun UserMediaListOptions.toModel() = MediaListOptionModel().also {
    it.rowOrder = getRowOrder(rowOrder!!)
    it.scoreFormat = scoreFormat!!.ordinal
    it.animeList = animeList?.let {
        MediaListOptionTypeModel().also { optionModel ->
            optionModel.advancedScoringEnabled = it.advancedScoringEnabled == true
            optionModel.advancedScoring =
                it.advancedScoring?.filterNotNull()?.toMutableList()
            optionModel.customLists = it.customLists?.filterNotNull()
        }
    }
    it.mangaList = mangaList?.let {
        MediaListOptionTypeModel().also { optionModel ->
            optionModel.advancedScoringEnabled = it.advancedScoringEnabled == true
            optionModel.advancedScoring =
                it.advancedScoring?.filterNotNull()?.toMutableList()
            optionModel.customLists = it.customLists?.filterNotNull()
        }
    }
}

fun MediaTitle.toModel() = MediaTitleModel(english, romaji, native, userPreferred)

fun MediaCoverImage.toModel() = MediaCoverImageModel(medium, large, extraLarge)

fun FuzzyDate.toModel() = FuzzyDateModel(
    year,
    month,
    day
).takeIf { it.year != null && it.month != null && it.day != null }

fun ActivityUnionQuery.OnTextActivity.toModel() = TextActivityModel().also { model ->
    model.id = id
    model.text = text ?: ""
    model.anilifiedText = anilify(model.text)
    model.textSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.anilifiedText)
    model.likeCount = likeCount
    model.replyCount = replyCount
    model.isSubscribed = isSubscribed ?: false
    model.type = type!!
    model.user = user?.activityUser?.toModel()
    model.likes = likes?.mapNotNull {
        it?.likeUsers?.toModel()
    }
    model.siteUrl = siteUrl
    model.createdAt = createdAt.toLong().prettyTime()
    model.userId = userId
    model.isLiked = isLiked ?: false
}

fun ActivityUnionQuery.OnListActivity.toModel() = ListActivityModel().also { model ->
    model.id = id
    model.media = media?.let {
        MediaModel().also { cModel ->
            cModel.id = it.id
            cModel.title = it.title?.mediaTitle?.toModel()
            cModel.type = it.type?.ordinal
            cModel.coverImage = it.coverImage?.mediaCoverImage?.toModel()
            cModel.bannerImage = it.bannerImage
            cModel.isAdult = it.isAdult == true
        }
    }
    model.status = status!!
    model.progress = progress ?: ""
    model.likeCount = likeCount
    model.replyCount = replyCount
    model.isSubscribed = isSubscribed ?: false
    model.type = type!!
    model.user = user?.activityUser?.toModel()
    model.likes = likes?.mapNotNull {
        it?.likeUsers?.toModel()
    }
    model.siteUrl = siteUrl
    model.createdAt = createdAt.toLong().prettyTime()
    model.userId = userId
    model.isLiked = isLiked ?: false
}

fun ActivityUnionQuery.OnMessageActivity.toModel() = MessageActivityModel().also { model ->
    model.id = id
    model.message = message ?: ""
    model.messageAnilified = anilify(model.message)
    model.messageSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.messageAnilified)
    model.recipientId = recipientId
    model.messengerId = messengerId
    model.messenger = messenger?.messengerUser?.toModel()
    model.isPrivate = isPrivate ?: false
    model.likes = likes?.mapNotNull {
        it?.likeUsers?.toModel()
    }
    model.likeCount = likeCount
    model.replyCount = replyCount
    model.isSubscribed = isSubscribed ?: false
    model.type = type!!
    model.siteUrl = siteUrl
    model.createdAt = createdAt.toLong().prettyTime()
    model.isLiked = isLiked ?: false
}

fun ActivityInfoQuery.OnTextActivity.toModel() = TextActivityModel().also { model ->
    model.id = id
    model.text = text ?: ""
    model.anilifiedText = anilify(model.text)
    model.textSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.anilifiedText)
    model.likeCount = likeCount
    model.replyCount = replyCount
    model.isSubscribed = isSubscribed ?: false
    model.type = type!!
    model.user = user?.activityUser?.toModel()
    model.likes = likes?.mapNotNull {
        it?.likeUsers?.toModel()
    }
    model.replies = replies?.mapNotNull {
        it?.replyUsers?.toModel()
    }
    model.siteUrl = siteUrl
    model.createdAt = createdAt.toLong().prettyTime()
    model.userId = userId
    model.isLiked = isLiked ?: false
}

fun ActivityInfoQuery.OnListActivity.toModel() = ListActivityModel().also { model ->
    model.id = id
    model.media = media?.let {
        MediaModel().also { cModel ->
            cModel.id = it.id
            cModel.title = it.title?.mediaTitle?.toModel()
            cModel.type = it.type?.ordinal
            cModel.coverImage = it.coverImage?.mediaCoverImage?.toModel()
            cModel.bannerImage = it.bannerImage
        }
    }
    model.status = status!!
    model.progress = progress ?: ""
    model.likeCount = likeCount
    model.replyCount = replyCount
    model.isSubscribed = isSubscribed ?: false
    model.type = type!!
    model.user = user?.activityUser?.toModel()
    model.likes = likes?.mapNotNull {
        it?.likeUsers?.toModel()
    }
    model.replies = replies?.mapNotNull {
        it?.replyUsers?.toModel()
    }
    model.siteUrl = siteUrl
    model.createdAt = createdAt.toLong().prettyTime()
    model.userId = userId
    model.isLiked = isLiked ?: false
}

fun ActivityInfoQuery.OnMessageActivity.toModel() = MessageActivityModel().also { model ->
    model.id = id
    model.message = message ?: ""
    model.messageAnilified = anilify(model.message)
    model.messageSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.messageAnilified)
    model.recipientId = recipientId
    model.messengerId = messengerId
    model.messenger = messenger?.messengerUser?.toModel()
    model.isPrivate = isPrivate ?: false

    model.likes = likes?.mapNotNull {
        it?.likeUsers?.toModel()
    }
    model.replies = replies?.mapNotNull {
        it?.replyUsers?.toModel()
    }
    model.likeCount = likeCount
    model.replyCount = replyCount
    model.isSubscribed = isSubscribed ?: false
    model.type = type!!
    model.siteUrl = siteUrl
    model.createdAt = createdAt.toLong().prettyTime()
    model.isLiked = isLiked ?: false
}


fun LikeUsers.toModel() = UserModel().also { fModel ->
    fModel.id = id
    fModel.name = name
    fModel.avatar = avatar?.let {
        UserAvatarModel(it.medium, it.large)
    }
    fModel.isBlocked = isBlocked ?: false
    fModel.isFollowing = isFollowing ?: false
    fModel.isFollower = isFollower ?: false
}


fun ReplyUsers.toModel() = ActivityReplyModel().also { model ->
    model.id = id
    model.activityId = activityId
    model.userId = userId
    model.isLiked = isLiked ?: false
    model.likeCount = likeCount
    model.text = text ?: ""
    model.anilifiedText = anilify(model.text)
    model.textSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.anilifiedText)
    model.user = user?.activityUser?.let {
        UserModel().also { fModel ->
            fModel.id = it.id
            fModel.name = it.name
            fModel.avatar = it.avatar?.let {
                UserAvatarModel(it.medium, it.large)
            }
        }
    }
    model.createdAt = createdAt.toLong().prettyTime()
}


fun ActivityUser.toModel() = UserModel().also { model ->
    model.id = id
    model.name = name
    model.avatar =
        avatar?.let {
            UserAvatarModel(it.medium, it.large)
        }
}

fun MessengerUser.toModel() = UserModel().also { model ->
    model.id = id
    model.name = name
    model.avatar =
        avatar?.let {
            UserAvatarModel(it.medium, it.large)
        }
}


fun MediaSocialFollowingQuery.MediaList.toModel() = MediaSocialFollowingModel().also { model ->
    model.id = id
    model.score = score
    model.type = media?.type?.ordinal
    model.status = status?.ordinal
    model.user = user?.let {
        UserModel().also { u ->
            u.id = it.id
            u.name = it.name
            u.avatar = it.avatar?.let { ava ->
                UserAvatarModel(ava.medium, ava.large)
            }
            u.mediaListOptions = it.mediaListOptions?.let { opt ->
                MediaListOptionModel().also { lModel ->
                    lModel.scoreFormat = opt.scoreFormat?.ordinal
                }
            }
        }
    }
}


fun NotificationMediaContent.toModel() =
    MediaModel().also { m ->
        m.id = id
        m.title = title?.mediaTitle?.toModel()
        m.coverImage = coverImage?.mediaCoverImage?.toModel()
        m.bannerImage = bannerImage
        m.format = format?.ordinal
        m.isAdult = isAdult == true
        m.type = type?.ordinal
    }

