package com.revolgenx.anilib.media.ui.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.revolgenx.anilib.MediaListEntryQuery
import com.revolgenx.anilib.MediaOverViewQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.ui.model.AiringAtModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.TimeUntilAiringModel
import com.revolgenx.anilib.character.ui.model.CharacterConnectionModel
import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.character.ui.model.CharacterImageModel
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.character.ui.model.CharacterNameModel
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.Media
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.studio.ui.model.StudioConnectionModel
import com.revolgenx.anilib.studio.ui.model.StudioEdgeModel
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.type.CharacterRole
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaRelation
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaSource
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.type.MediaType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class MediaModel(
    val id: Int = -1,
    val averageScore: Int? = null,
    val bannerImage: String? = null,
    val chapters: Int? = null,
    val characters: CharacterConnectionModel? = null,
    val staff: StaffModel? = null,
//    val staffs: List<StaffModel>? = null,
    var staffRole: String? = null,
//    val staffs: StaffConnectionModel? = null,
    val countryOfOrigin: String? = null,
    val coverImage: MediaCoverImageModel? = null,
    val description: String? = null,
    val duration: Int? = null,
    val episodes: Int? = null,
    val externalLinks: List<MediaExternalLinkModel>? = null,
    val favourites: Int? = null,
    val format: MediaFormat? = null,
    val genres: List<String>? = null,
    val hashtag: String? = null,
    val idMal: Int = -1,
    val isAdult: Boolean = false,
    val isFavourite: Boolean = false,
    val meanScore: Int? = null,
    val mediaListEntry: MediaListModel? = null,
    val nextAiringEpisode: AiringScheduleModel? = null,
    val popularity: Int? = null,
//    val rankings: List<MediaRankModel>? = null,
//    val recommendations: RecommendationConnectionModel? = null,
    val relations: MediaConnectionModel? = null,
//    val reviews: ReviewConnection? = null,
    val season: MediaSeason? = null,
    val seasonInt: Int? = null,
    val seasonYear: Int? = null,
    val siteUrl: String? = null,
    val source: MediaSource? = null,
    val startDate: FuzzyDateModel? = null,
    val endDate: FuzzyDateModel? = null,
//    val stats: MediaStatsModel? = null,
    val status: MediaStatus? = null,
    val streamingEpisodes: List<StreamingEpisodeModel>? = null,
    val studios: StudioConnectionModel? = null,
//    val studios: List<StudioModel>? = null,
    val synonyms: List<String>? = null,
    val tags: List<MediaTagModel>? = null,
    val title: MediaTitleModel? = null,
    val trailer: MediaTrailerModel? = null,
    val trending: Int? = null,
//    val trends: MediaTrendConnectionModel? = null,
    val type: MediaType? = null,
    val updatedAt: Int? = null,
    val volumes: Int? = null,

    // for StudioScreen
    var studio: StudioModel? = null,
    var character: CharacterModel? = null,
    var characterRole: CharacterRole? = null,

    ) : BaseModel {
    val isAnime get() = type.isAnime()
}


fun MediaType?.isAnime() = this == MediaType.ANIME
fun MediaType?.isManga() = this == MediaType.MANGA


fun Media.toModel(): MediaModel {
    val coverImage = coverImage?.mediaCoverImage?.toModel()
    return MediaModel(
        id = id,
        title = title?.mediaTitle?.toModel(),
        popularity = popularity,
        favourites = favourites,
        format = format,
        type = type,
        episodes = episodes,
        duration = duration,
        chapters = chapters,
        volumes = volumes,
        status = status,
        coverImage = coverImage,
        genres = genres?.filterNotNull(),
        averageScore = averageScore,
        season = season,
        seasonYear = seasonYear,
        startDate = startDate?.fuzzyDate?.toModel(),
        endDate = endDate?.fuzzyDate?.toModel(),
        bannerImage = bannerImage ?: coverImage?.extraLargeImage,
        isAdult = isAdult ?: false,
        mediaListEntry = mediaListEntry?.let { list ->
            MediaListModel(
                progress = list.progress ?: 0,
                status = list.status
            )
        }
    )
}

fun MediaOverViewQuery.Media.toModel(): MediaModel {
    val coverImage = coverImage?.mediaCoverImage?.toModel()
    return MediaModel(
        id = id,
        title = title?.mediaTitle?.toModel(),
        coverImage = coverImage,
        bannerImage = bannerImage ?: coverImage?.large,
        siteUrl = siteUrl,
        startDate = startDate?.fuzzyDate?.toModel(),
        endDate = endDate?.fuzzyDate?.toModel(),
        genres = genres?.filterNotNull(),
        episodes = episodes,
        chapters = chapters,
        volumes = volumes,
        averageScore = averageScore,
        meanScore = meanScore,
        duration = duration,
        status = status,
        format = format,
        type = type,
        isAdult = isAdult ?: false,
        popularity = popularity,
        favourites = favourites,
        season = season,
        seasonYear = seasonYear,
        description = description,
        source = source,
        hashtag = hashtag,
        synonyms = synonyms?.filterNotNull(),
        isFavourite = isFavourite,
        streamingEpisodes = streamingEpisodes?.mapNotNull {
            it?.let {
                StreamingEpisodeModel(
                    title = it.title,
                    thumbnail = it.thumbnail,
                    url = it.url,
                    site = it.site
                )
            }
        },
        mediaListEntry = mediaListEntry?.let {
            MediaListModel(it.id, status = it.status)
        },
        nextAiringEpisode = nextAiringEpisode?.let {
            AiringScheduleModel(
                episode = it.episode,
                timeUntilAiring = it.timeUntilAiring,
                timeUntilAiringModel =
                TimeUntilAiringModel(it.timeUntilAiring.toLong()),
                airingAt = it.airingAt,
                airingAtModel = AiringAtModel(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(
                            it.airingAt.toLong()
                        ), ZoneOffset.systemDefault()
                    )
                )
            )
        },
        relations = relations?.let {
            MediaConnectionModel(
                edges = it.edges?.map { edge ->
                    MediaEdgeModel(
                        relationType = edge?.relationType,
                        node = edge?.node?.let { node ->
                            val nodeCoverImage = node.coverImage?.mediaCoverImage?.toModel()
                            MediaModel(
                                id = node.id,
                                title = node.title?.mediaTitle?.toModel(),
                                format = node.format,
                                type = node.type,
                                status = node.status,
                                averageScore = node.averageScore,
                                seasonYear = node.seasonYear,
                                coverImage = nodeCoverImage,
                                bannerImage = node.bannerImage ?: nodeCoverImage?.extraLargeImage
                            )
                        }
                    )
                }?.sortedWith { a, b ->
                    val mA = a.relationType ?: MediaRelation.UNKNOWN__
                    val mB = b.relationType ?: MediaRelation.UNKNOWN__
                    if (mA.ordinal < 10 && mB.ordinal < 10) {
                        mA.ordinal - mB.ordinal
                    } else {
                        if (mA.ordinal > 9) {
                            -1
                        } else {
                            1
                        }
                    }
                }
            )
        },

        externalLinks = externalLinks?.mapNotNull { linkData ->
            linkData?.let {
                MediaExternalLinkModel(it.id, it.site, it.url)
            }
        },

        tags = tags?.mapNotNull { tagData ->
            tagData?.let {
                MediaTagModel(
                    name = it.name,
                    description = it.description,
                    category = it.category,
                    isMediaSpoilerTag = it.isMediaSpoiler == true,
                    rank = it.rank,
                    isAdult = it.isAdult == true
                )
            }
        },

        trailer = trailer?.let {
            MediaTrailerModel(it.id, it.site, it.thumbnail)
        },

        studios = studios?.let {
            StudioConnectionModel(
                edges = it.edges?.map {
                    StudioEdgeModel(
                        isMain = it?.isMain == true,
                        node = it?.node?.let { node ->
                            StudioModel(node.id, name = node.name)
                        }
                    )
                }
            )
        },

        characters = characters?.let {
            CharacterConnectionModel(
                edges = it.edges?.map { edge ->
                    CharacterEdgeModel(
                        role = edge?.role,
                        node = edge?.node?.let { node ->
                            CharacterModel(
                                id = node.id,
                                name = node.name?.let {
                                    CharacterNameModel(it.full)
                                },
                                image = node.image?.let {
                                    CharacterImageModel(it.medium, it.large)
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}


@StringRes
fun MediaFormat?.toStringRes(): Int? {
    return when (this) {
        MediaFormat.TV -> R.string.tv
        MediaFormat.TV_SHORT -> R.string.tv_short
        MediaFormat.MOVIE -> R.string.movie
        MediaFormat.SPECIAL -> R.string.special
        MediaFormat.OVA -> R.string.ova
        MediaFormat.ONA -> R.string.ona
        MediaFormat.MANGA -> R.string.manga
        MediaFormat.MUSIC -> R.string.music
        MediaFormat.NOVEL -> R.string.novel
        MediaFormat.ONE_SHOT -> R.string.one_shot
        else -> null
    }
}

@StringRes
fun MediaType?.toStringRes(): Int {
    return when (this) {
        MediaType.ANIME -> R.string.anime
        MediaType.MANGA -> R.string.manga
        else -> R.string.unknown
    }
}

@StringRes
fun MediaStatus?.toStringRes(): Int {
    return when (this) {
        MediaStatus.FINISHED -> R.string.finished
        MediaStatus.RELEASING -> R.string.releasing
        MediaStatus.NOT_YET_RELEASED -> R.string.not_yet_released
        MediaStatus.CANCELLED -> R.string.cancelled
        MediaStatus.HIATUS -> R.string.hiatus
        else -> R.string.unknown
    }
}

fun MediaStatus?.toColor(): Color {
    return when (this) {
        MediaStatus.FINISHED -> Color(0xFF02A4F8)
        MediaStatus.RELEASING -> Color(0xFF00C853)
        MediaStatus.NOT_YET_RELEASED -> Color(0xFF673AB7)
        MediaStatus.CANCELLED -> Color(0xFFD50000)
        MediaStatus.HIATUS -> Color(0xFFFF6E40)
        else -> Color(0xFFD50000)
    }
}


fun Int.toMediaStatus() = MediaStatus.values().getOrNull(this)
fun Int.toMediaSeason() = MediaSeason.values().getOrNull(this)
fun Int.toMediaFormat() = MediaFormat.values().getOrNull(this)

