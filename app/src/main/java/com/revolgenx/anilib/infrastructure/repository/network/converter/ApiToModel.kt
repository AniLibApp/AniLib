package com.revolgenx.anilib.infrastructure.repository.network.converter

import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.MediaOverViewQuery
import com.revolgenx.anilib.MediaWatchQuery
import com.revolgenx.anilib.data.model.*
import com.revolgenx.anilib.data.model.entry.AdvancedScore
import com.revolgenx.anilib.data.model.entry.MediaEntryListModel
import com.revolgenx.anilib.data.model.list.MediaListOptionModel
import com.revolgenx.anilib.data.model.list.MediaListOptionTypeModel
import com.revolgenx.anilib.fragment.*
import com.revolgenx.anilib.util.pmap
import kotlinx.coroutines.runBlocking

fun <T : CommonMediaModel> NarrowMediaContent.getCommonMedia(model: T): T {
    model.mediaId = id()
    model.title = title()?.fragments()?.mediaTitle()?.toModel()
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
    model.bannerImage = bannerImage() ?: model.coverImage!!.extraLarge

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
    model.bannerImage = bannerImage() ?: model.coverImage!!.extraLarge

    model.isAdult = isAdult ?: false
    model.mediaEntryListModel = mediaListEntry()?.let {
        MediaEntryListModel(
            it.progress() ?: 0,
            it.status()?.ordinal
        )
    }
    return model
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
    it.userId = id()
    it.userName = name()
    it.mediaListOption = mediaListOptions()?.let {
        MediaListOptionModel().apply {
            scoreFormat = it.scoreFormat()!!.ordinal
            animeList = it.animeList()?.let {
                MediaListOptionTypeModel().also { typeModel ->
                    typeModel.advancedScoringEnabled = it.advancedScoringEnabled()!!
                    typeModel.advancedScoring = it.advancedScoring()?.map { AdvancedScore(it, 0.0) }
                }
            }
            mangaList = it.mangaList()?.let {
                MediaListOptionTypeModel().also { typeModel ->
                    typeModel.advancedScoringEnabled = it.advancedScoringEnabled()!!
                    typeModel.advancedScoring = it.advancedScoring()?.map { AdvancedScore(it, 0.0) }
                }
            }
        }
    }
    it.avatar = avatar()?.let {
        UserAvatarImageModel().also { img ->
            img.large = avatar()!!.large()
            img.medium = avatar()!!.medium()
        }
    }
    it.bannerImage = bannerImage() ?: ""
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
        DateModel().also { date ->
            date.day = it.day()
            date.month = it.month()
            date.year = it.year()
        }
    }
    it.endDate = completedAt()?.let {
        DateModel().also { date ->
            date.day = it.day()
            date.month = it.month()
            date.year = it.year()
        }
    }

}


fun MediaOverViewQuery.Media.toMediaOverviewModel() = MediaOverviewModel().also {
    runBlocking {

        it.mediaId = id()
        it.title = title()?.fragments()?.mediaTitle()?.toModel()
        it.startDate = startDate()?.fragments()?.fuzzyDate()?.toModel()
        it.endDate = endDate()?.fragments()?.fuzzyDate()?.toModel()
        it.genres = genres()
        it.episodes = episodes()?.toString()
        it.chapters = chapters()?.toString()
        it.volumes = volumes()?.toString()
        it.averageScore = averageScore()
        it.meanScore = meanScore()
        it.duration = duration()?.toString()
        it.status = status()?.ordinal
        it.format = format()?.ordinal
        it.type = type()?.ordinal
        it.isAdult = isAdult ?: false
        it.popularity = popularity()
        it.favourites = favourites()
        it.season = season()?.ordinal
        it.seasonYear = seasonYear()
        it.description = description()
        it.source = source()?.ordinal
        it.hashTag = hashtag()
        it.airingTimeModel = nextAiringEpisode()?.let {
            AiringTimeModel().also { model ->
                model.episode = it.episode()
                model.airingTime =
                    AiringTime().also { ti -> ti.time = it.timeUntilAiring().toLong() }
            }
        }
        it.relationship = relations()?.edges()?.pmap {
            MediaRelationshipModel().also { rel ->
                rel.relationshipType = it.relationType()?.ordinal
                it.node()?.let { node ->
                    rel.mediaId = node.id()
                    rel.title = node.title()?.fragments()?.mediaTitle()?.toModel()
                    rel.format = node.format()?.ordinal
                    rel.type = node.type()?.ordinal
                    rel.status = node.status()?.ordinal
                    rel.averageScore = node.averageScore()
                    rel.seasonYear = node.seasonYear()
                    rel.coverImage = node.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
                    rel.bannerImage = node.bannerImage() ?: rel.coverImage?.largeImage
                }
            }
        }
        it.externalLink = externalLinks()?.pmap {
            MediaExternalLinkModel().also { link ->
                link.linkId = it.id()
                link.site = it.site()
                link.url = it.url()
            }
        }
        it.tags = tags()?.pmap {
            MediaTagsModel().also { tag ->
                tag.name = it.name()
                tag.isSpoilerTag = it.isMediaSpoiler ?: false
            }
        }
        it.trailer = trailer()?.let {
            MediaTrailerModel().also { trailer ->
                trailer.id = it.id()
                trailer.site = it.site()
                trailer.thumbnail = it.thumbnail()
            }
        }
        it.studios = studios()?.edges()?.pmap {
            MediaStudioModel().also { studio ->
                it.node()?.let { node ->
                    studio.studioName = node.name()
                    studio.isAnimationStudio = node.isAnimationStudio
                    studio.studioId = node.id()
                }
            }
        }
    }
}

fun MediaWatchQuery.Media.toModel() = streamingEpisodes()?.map {
    MediaWatchModel().also { model ->
        model.site = it.site()
        model.thumbnail = it.thumbnail()
        model.title = it.title()
        model.url = it.url()
    }
}

fun MediaTitle.toModel() = TitleModel(english(), romaji(), native_(), userPreferred())

fun MediaCoverImage.toModel() = CoverImageModel(medium(), large(), extraLarge())

fun FuzzyDate.toModel() = DateModel(year(), month(), day())