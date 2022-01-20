package com.revolgenx.anilib.infrastructure.repository.network.converter

import com.revolgenx.anilib.*
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.media.data.model.MediaTitleModel
import com.revolgenx.anilib.entry.data.model.AdvancedScoreModel
import com.revolgenx.anilib.entry.data.model.MediaEntryListModel
import com.revolgenx.anilib.data.model.list.MediaListCountModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionTypeModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.app.setting.data.model.getRowOrder
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.studio.data.model.MediaStudioModel
import com.revolgenx.anilib.user.data.model.UserAvatarModel
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.data.model.UserPrefModel
import com.revolgenx.anilib.fragment.*
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.*
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify
import com.revolgenx.anilib.util.prettyTime

fun <T : CommonMediaModel> NarrowMediaContent.getCommonMedia(model: T): T {
    model.mediaId = id()
    model.title = title()?.fragments()?.mediaTitle()?.toModel()
    model.description = description() ?: ""
    model.popularity = popularity()
    model.favourites = favourites()
    model.format = format()?.ordinal
    model.type = type()?.ordinal
    model.episodes = episodes()?.toString()
    model.duration = duration()?.toString()
    model.chapters = chapters()?.toString()
    model.volumes = volumes()?.toString()
    model.status = status()?.ordinal
    model.coverImage = coverImage()?.fragments()?.mediaCoverImage()?.toModel()
    model.genres = genres()
    model.averageScore = averageScore()
    model.season = season()?.ordinal
    model.seasonYear = seasonYear()

    model.startDate = startDate()?.fragments()?.fuzzyDate()?.toModel()
    model.endDate = endDate()?.fragments()?.fuzzyDate()?.toModel()
    model.bannerImage = bannerImage() ?: model.coverImage!!.largeImage

    model.isAdult = isAdult ?: false
    model.mediaEntryListModel = mediaListEntry()?.let {
        MediaEntryListModel(
            it.progress() ?: 0,
            it.status()?.ordinal
        )
    }
    return model
}

fun <T : CommonMediaModel> CommonMediaContent.getCommonMedia(model: T): T {
    model.mediaId = id()
    model.title = title()?.fragments()?.mediaTitle()?.toModel()
    model.format = format()?.ordinal
    model.type = type()?.ordinal
    model.status = status()?.ordinal
    model.coverImage = coverImage()?.fragments()?.mediaCoverImage()?.toModel()
    model.averageScore = averageScore()
    model.season = season()?.ordinal
    model.seasonYear = seasonYear()
    model.bannerImage = bannerImage() ?: model.coverImage!!.largeImage

    model.isAdult = isAdult ?: false
    model.mediaEntryListModel = mediaListEntry()?.let {
        MediaEntryListModel(
            it.progress() ?: 0,
            it.status()?.ordinal
        )
    }
    return model
}


fun NarrowMediaContent.toModel() = MediaModel().also {model->
    model.id = id()
    model.title = title()?.fragments()?.mediaTitle()?.toModel()
    model.description = description() ?: ""
    model.popularity = popularity()
    model.favourites = favourites()
    model.format = format()?.ordinal
    model.type = type()?.ordinal
    model.episodes = episodes()
    model.duration = duration()
    model.chapters = chapters()
    model.volumes = volumes()
    model.status = status()?.ordinal
    model.coverImage = coverImage()?.fragments()?.mediaCoverImage()?.toModel()
    model.genres = genres()
    model.averageScore = averageScore()
    model.season = season()?.ordinal
    model.seasonYear = seasonYear()

    model.startDate = startDate()?.fragments()?.fuzzyDate()?.toModel()
    model.endDate = endDate()?.fragments()?.fuzzyDate()?.toModel()
    model.bannerImage = bannerImage() ?: model.coverImage!!.largeImage

    model.isAdult = isAdult ?: false
    model.mediaListEntry = mediaListEntry()?.let {
        MediaListModel().also {model->
            model.progress = it.progress() ?: 0
            model.status = it.status()?.ordinal
        }
    }
    return model
}


fun CommonMediaContent.toModel() = MediaModel().also {model->
    model.id = id()
    model.title = title()?.fragments()?.mediaTitle()?.toModel()
    model.format = format()?.ordinal
    model.type = type()?.ordinal
    model.status = status()?.ordinal
    model.coverImage = coverImage()?.fragments()?.mediaCoverImage()?.toModel()
    model.averageScore = averageScore()
    model.season = season()?.ordinal
    model.seasonYear = seasonYear()
    model.bannerImage = bannerImage() ?: model.coverImage!!.largeImage

    model.isAdult = isAdult ?: false
    model.mediaListEntry = mediaListEntry()?.let {
        MediaListModel().also { list->
            list.progress = it.progress() ?: 0
            list.status = it.status()?.ordinal
        }
    }
}


fun BasicMediaContent.toBasicMediaContent() = CommonMediaModel().also { media ->
    media.mediaId = id()
    media.title = title()?.fragments()?.mediaTitle()?.toModel()
    media.coverImage = coverImage()?.fragments()?.mediaCoverImage()?.toModel()
    media.type = type()?.ordinal
    media.isAdult = isAdult ?: false
    media.bannerImage = bannerImage()
}

fun BasicUserQuery.User.toBasicUserModel() = UserPrefModel().also {
    it.id = id()
    it.name = name()
    it.unreadNotificationCount = unreadNotificationCount()
    it.mediaListOptions = mediaListOptions()?.fragments()?.userMediaListOptions()?.toModel()
    it.options = options()?.fragments()?.userMediaOptions()?.toModel()
}


fun MediaListContent.toListEditorMediaModel() = EntryListEditorMediaModel().also {
    it.mediaId = mediaId()
    it.userId = userId()
    it.listId = id()
    it.status = status()?.ordinal
    it.notes = notes() ?: ""
    it.progress = progress() ?: 0
    it.progressVolumes = progressVolumes() ?: 0
    it.private = private_() ?: false
    it.repeat = repeat()
    it.startDate = startedAt()?.let {
        FuzzyDateModel().also { date ->
            date.day = it.day()
            date.month = it.month()
            date.year = it.year()
        }
    }
    it.endDate = completedAt()?.let {
        FuzzyDateModel().also { date ->
            date.day = it.day()
            date.month = it.month()
            date.year = it.year()
        }
    }
    it.type = media()!!.type()?.ordinal
}


fun MediaWatchQuery.Media.toModel() = streamingEpisodes()?.map {
    MediaStreamingEpisodeModel().also { model ->
        model.site = it.site()
        model.thumbnail = it.thumbnail()
        model.title = it.title()
        model.url = it.url()
    }
}


fun UserMediaOptions.toModel() = UserOptionsModel(
    titleLanguage()!!.ordinal,
    displayAdultContent()!!,
    airingNotifications()!!,
    profileColor()
)

fun UserMediaListOptions.toModel() = MediaListOptionModel().also {
    it.rowOrder = getRowOrder(rowOrder()!!)
    it.scoreFormat = scoreFormat()!!.ordinal
    it.animeList = animeList()?.let {
        MediaListOptionTypeModel().also { optionModel ->
            optionModel.advancedScoringEnabled = it.advancedScoringEnabled()!!
            optionModel.advancedScoring =
                it.advancedScoring()?.map { AdvancedScoreModel(it, 0.0) }?.toMutableList()
            optionModel.customLists = it.customLists()
        }
    }
    it.animeList = mangaList()?.let {
        MediaListOptionTypeModel().also { optionModel ->
            optionModel.customLists = it.customLists()
        }
    }
}

fun MediaTitle.toModel() = MediaTitleModel(english(), romaji(), native_(), userPreferred())

fun MediaCoverImage.toModel() = MediaCoverImageModel().also{ model->
    model.medium = medium()
    model.large = large()
    model.extraLarge = extraLarge()
}

fun FuzzyDate.toModel() = FuzzyDateModel(year(), month(), day())

fun UserListCount.toModel() = MediaListCountModel(count(), status()!!.ordinal)

fun StudioInfo.toModel() = MediaStudioModel().also { studio ->
    studio.studioName = name()
    studio.isAnimationStudio = isAnimationStudio
    studio.id = id()
}


fun ActivityUnionQuery.AsTextActivity.toModel() = TextActivityModel().also { model ->
    model.id = id()
    model.text = text() ?: ""
    model.anilifiedText = anilify(model.text)
    model.textSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.anilifiedText)
    model.likeCount = likeCount()
    model.replyCount = replyCount()
    model.isSubscribed = isSubscribed ?: false
    model.type = type()!!
    model.user = user()?.fragments()?.activityUser()?.toModel()
    model.likes = likes()?.map {
        it.fragments().likeUsers().toModel()
    }
    model.siteUrl = siteUrl()
    model.createdAt = createdAt().toLong().prettyTime()
    model.userId = userId()
    model.isLiked = isLiked ?: false
}

fun ActivityUnionQuery.AsListActivity.toModel() = ListActivityModel().also { model ->
    model.id = id()
    model.media = media()?.let {
        CommonMediaModel().also { cModel ->
            cModel.mediaId = it.id()
            cModel.title = it.title()?.fragments()?.mediaTitle()?.toModel()
            cModel.type = it.type()?.ordinal
            cModel.coverImage = it.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
            cModel.bannerImage = it.bannerImage()
        }
    }
    model.status = status()!!
    model.progress = progress() ?: ""
    model.likeCount = likeCount()
    model.replyCount = replyCount()
    model.isSubscribed = isSubscribed ?: false
    model.type = type()!!
    model.user = user()?.fragments()?.activityUser()?.toModel()
    model.likes = likes()?.map {
        it.fragments().likeUsers().toModel()
    }
    model.siteUrl = siteUrl()
    model.createdAt = createdAt().toLong().prettyTime()
    model.userId = userId()
    model.isLiked = isLiked ?: false
}

fun ActivityUnionQuery.AsMessageActivity.toModel() = MessageActivityModel().also { model ->
    model.id = id()
    model.message = message() ?: ""
    model.messageAnilified = anilify(model.message)
    model.messageSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.messageAnilified)
    model.recipientId = recipientId()
    model.messengerId = messengerId()
    model.messenger = messenger()?.fragments()?.messengerUser()?.toModel()
    model.isPrivate = isPrivate ?: false
    model.likes = likes()?.map {
        it.fragments().likeUsers().toModel()
    }
    model.likeCount = likeCount()
    model.replyCount = replyCount()
    model.isSubscribed = isSubscribed ?: false
    model.type = type()!!
    model.siteUrl = siteUrl()
    model.createdAt = createdAt().toLong().prettyTime()
    model.isLiked = isLiked ?: false
}

fun ActivityInfoQuery.AsTextActivity.toModel() = TextActivityModel().also { model ->
    model.id = id()
    model.text = text() ?: ""
    model.anilifiedText = anilify(model.text)
    model.textSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.anilifiedText)
    model.likeCount = likeCount()
    model.replyCount = replyCount()
    model.isSubscribed = isSubscribed ?: false
    model.type = type()!!
    model.user = user()?.fragments()?.activityUser()?.toModel()
    model.likes = likes()?.map {
        it.fragments().likeUsers().toModel()
    }
    model.replies = replies()?.map {
        it.fragments().replyUsers().toModel()
    }
    model.siteUrl = siteUrl()
    model.createdAt = createdAt().toLong().prettyTime()
    model.userId = userId()
    model.isLiked = isLiked ?: false
}

fun ActivityInfoQuery.AsListActivity.toModel() = ListActivityModel().also { model ->
    model.id = id()
    model.media = media()?.let {
        CommonMediaModel().also { cModel ->
            cModel.mediaId = it.id()
            cModel.title = it.title()?.fragments()?.mediaTitle()?.toModel()
            cModel.type = it.type()?.ordinal
            cModel.coverImage = it.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
            cModel.bannerImage = it.bannerImage()
        }
    }
    model.status = status()!!
    model.progress = progress() ?: ""
    model.likeCount = likeCount()
    model.replyCount = replyCount()
    model.isSubscribed = isSubscribed ?: false
    model.type = type()!!
    model.user = user()?.fragments()?.activityUser()?.toModel()
    model.likes = likes()?.map {
        it.fragments().likeUsers().toModel()
    }
    model.replies = replies()?.map {
        it.fragments().replyUsers().toModel()
    }
    model.siteUrl = siteUrl()
    model.createdAt = createdAt().toLong().prettyTime()
    model.userId = userId()
    model.isLiked = isLiked ?: false
}

fun ActivityInfoQuery.AsMessageActivity.toModel() = MessageActivityModel().also { model ->
    model.id = id()
    model.message = message() ?: ""
    model.messageAnilified = anilify(model.message)
    model.messageSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.messageAnilified)
    model.recipientId = recipientId()
    model.messengerId = messengerId()
    model.messenger = messenger()?.fragments()?.messengerUser()?.toModel()
    model.isPrivate = isPrivate ?: false

    model.likes = likes()?.map {
        it.fragments().likeUsers().toModel()
    }
    model.replies = replies()?.map {
        it.fragments().replyUsers().toModel()
    }
    model.likeCount = likeCount()
    model.replyCount = replyCount()
    model.isSubscribed = isSubscribed ?: false
    model.type = type()!!
    model.siteUrl = siteUrl()
    model.createdAt = createdAt().toLong().prettyTime()
    model.isLiked = isLiked ?: false
}


fun LikeUsers.toModel() = UserModel().also { fModel ->
    fModel.id = id()
    fModel.name = name()
    fModel.avatar = avatar()?.let {
        UserAvatarModel().also { uModel ->
            uModel.large = it.large()
            uModel.medium = it.medium()
        }
    }
    fModel.isBlocked = isBlocked ?: false
    fModel.isFollowing = isFollowing ?: false
    fModel.isFollower = isFollower ?: false
}


fun ReplyUsers.toModel() = ActivityReplyModel().also { model ->
    model.id = id()
    model.activityId = activityId()
    model.userId = userId()
    model.isLiked = isLiked ?: false
    model.likeCount = likeCount()
    model.text = text() ?: ""
    model.anilifiedText = anilify(model.text)
    model.textSpanned = AlMarkwonFactory.getMarkwon().toMarkdown(model.anilifiedText)
    model.user = user()?.fragments()?.activityUser()?.let {
        UserModel().also { fModel ->
            fModel.id = it.id()
            fModel.name = it.name()
            fModel.avatar = it.avatar()?.let {
                UserAvatarModel().also { uModel ->
                    uModel.large = it.large()
                    uModel.medium = it.medium()
                }
            }
        }
    }
    model.createdAt = createdAt().toLong().prettyTime()
}


fun ActivityUser.toModel() = UserModel().also { model ->
    model.id = id()
    model.name = name()
    model.avatar =
        avatar()?.let {
            UserAvatarModel().also { uModel ->
                uModel.large = it.large()
                uModel.medium = it.medium()
            }
        }
}

fun MessengerUser.toModel() = UserModel().also { model ->
    model.id = id()
    model.name = name()
    model.avatar =
        avatar()?.let {
            UserAvatarModel().also { uModel ->
                uModel.large = it.large()
                uModel.medium = it.medium()
            }
        }
}


fun MediaSocialFollowingQuery.MediaList.toModel() = MediaSocialFollowingModel().also { model ->
    model.id = id()
    model.score = score()
    model.type = media()?.type()?.ordinal
    model.status = status()?.ordinal
    model.user = user()?.let {
        UserModel().also { u ->
            u.id = it.id()
            u.name = it.name()
            u.avatar = it.avatar()?.let { ava ->
                UserAvatarModel().also { avatarModel ->
                    avatarModel.medium = ava.medium()
                    avatarModel.large = ava.large()
                }
            }
            u.mediaListOptions = it.mediaListOptions()?.let { opt ->
                MediaListOptionModel().also { lModel ->
                    lModel.scoreFormat = opt.scoreFormat()?.ordinal
                }
            }
        }
    }
}


//TODO DELETE
fun AlMediaListCollectionQuery.Entry.toModel() = AlMediaListModel().also { model ->
    model.mediaListId = id()
    model.progress = progress()
    model.score = score()

    model.listStartDate =
        startedAt()?.fragments()?.fuzzyDate()?.toModel()
    model.listCompletedDate =
        completedAt()?.fragments()?.fuzzyDate()?.toModel()
    model.listUpdatedDate = updatedAt()
    model.listCreatedDate = createdAt()

    model.scoreFormat =
        user()?.mediaListOptions()?.scoreFormat()?.ordinal

    media()?.let { media ->
        model.mediaId = media.id()
        model.episodes = media.episodes()?.toString()
        model.chapters = media.chapters()?.toString()
        model.title = media.title()?.fragments()?.mediaTitle()?.toModel()
        model.coverImage = media.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
        model.bannerImage = media.bannerImage() ?: model.coverImage?.extraLarge
        model.startDate =
            media.startDate()?.fragments()?.fuzzyDate()?.toModel()
        model.endDate =
            media.endDate()?.fragments()?.fuzzyDate()?.toModel()
        model.averageScore = media.averageScore()
        model.popularity = media.popularity()
        model.type = media.type()?.ordinal
        model.genres = media.genres()
        model.format = media.format()?.ordinal
        model.status = media.status()?.ordinal
        model.synonyms = media.synonyms()
    }
}
