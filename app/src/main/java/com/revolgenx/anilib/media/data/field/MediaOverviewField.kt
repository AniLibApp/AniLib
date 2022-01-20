package com.revolgenx.anilib.media.data.field

import com.revolgenx.anilib.MediaOverViewQuery
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.airing.data.model.TimeUntilAiringModel
import com.revolgenx.anilib.character.data.model.*
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.media.data.model.*
import com.revolgenx.anilib.studio.data.model.StudioConnectionModel
import com.revolgenx.anilib.studio.data.model.StudioEdgeModel
import com.revolgenx.anilib.studio.data.model.StudioModel

class MediaOverviewField :
    BaseField<MediaOverViewQuery>() {
    var mediaId: Int? = null
    override fun toQueryOrMutation(): MediaOverViewQuery {
        return MediaOverViewQuery.builder()
            .mediaId(mediaId)
            .build()
    }
}

fun MediaOverViewQuery.Media.toModel() = MediaModel().also {
    it.id = id()
    it.title = title()?.fragments()?.mediaTitle()?.toModel()
    it.startDate = startDate()?.fragments()?.fuzzyDate()?.toModel()
    it.endDate = endDate()?.fragments()?.fuzzyDate()?.toModel()
    it.genres = genres()
    it.episodes = episodes()
    it.chapters = chapters()
    it.volumes = volumes()
    it.averageScore = averageScore()
    it.meanScore = meanScore()
    it.duration = duration()
    it.status = status()?.ordinal
    it.format = format()?.ordinal
    it.type = type()?.ordinal
    it.isAdult = isAdult ?: false
    it.popularity = popularity()
    it.favourites = favourites()
    it.season = season()?.ordinal
    it.seasonYear = seasonYear()
    it.description = description()
    it.source = source().toSource()
    it.hashtag = hashtag()
    it.synonyms = synonyms()
    it.nextAiringEpisode = nextAiringEpisode()?.let {
        AiringScheduleModel().also { model ->
            model.episode = it.episode()
            model.timeUntilAiring = it.timeUntilAiring()
            model.timeUntilAiringModel =
                TimeUntilAiringModel().also { ti -> ti.time = it.timeUntilAiring().toLong() }
        }
    }
    it.relations = relations()?.let {
        MediaConnectionModel().also { m ->
            m.edges = it.edges()?.map { edge ->
                MediaEdgeModel().also { model ->
                    model.relationType = edge.relationType().toRelation()
                    model.node = edge.node()?.let { node ->
                        MediaModel().also { rel ->
                            rel.id = node.id()
                            rel.title = node.title()?.fragments()?.mediaTitle()?.toModel()
                            rel.format = node.format()?.ordinal
                            rel.type = node.type()?.ordinal
                            rel.status = node.status()?.ordinal
                            rel.averageScore = node.averageScore()
                            rel.seasonYear = node.seasonYear()
                            rel.coverImage =
                                node.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
                            rel.bannerImage = node.bannerImage() ?: rel.coverImage?.largeImage
                        }
                    }
                }
            }
        }
    }

    it.externalLinks = externalLinks()?.map {
        MediaExternalLinkModel().also { link ->
            link.id = it.id()
            link.site = it.site()
            link.url = it.url()
        }
    }

    it.tags = tags()?.map {
        MediaTagModel().apply {
            name = it.name()
            description = it.description()
            category = it.category()
            isMediaSpoilerTag = it.isMediaSpoiler == true
            rank = it.rank()
            isAdult = it.isAdult == true
        }
    }

    it.trailer = trailer()?.let {
        MediaTrailerModel().also { trailer ->
            trailer.id = it.id()
            trailer.site = it.site()
            trailer.thumbnail = it.thumbnail()
        }
    }

    it.studios = studios()?.let {
        StudioConnectionModel().also { model ->
            model.edges = it.edges()?.map {
                StudioEdgeModel().also { edge ->
                    edge.isMain = it.isMain
                    edge.node = it.node()?.let { node ->
                        StudioModel().also { studio ->
                            studio.id = node.id()
                            studio.studioName = node.name()
                        }
                    }
                }
            }
        }
    }

    it.characters = characters()?.let {
        CharacterConnectionModel().also { c ->
            c.edges = it.edges()?.map { edge ->
                CharacterEdgeModel().also { model ->
                    model.role = edge.role()?.ordinal
                    model.node = edge.node()?.let { node ->
                        CharacterModel().also { character ->
                            character.id = node.id()
                            character.name = node.name()?.let {
                                CharacterNameModel().also { model ->
                                    model.full = it.full()
                                }
                            }
                            character.image = node.image()?.let {
                                CharacterImageModel().apply {
                                    large = it.large()
                                    medium = it.medium()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

