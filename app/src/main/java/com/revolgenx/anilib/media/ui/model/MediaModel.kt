package com.revolgenx.anilib.media.ui.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.Media
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.type.MediaType

data class MediaModel(
    val id: Int = -1,
    val averageScore: Int? = null,
    val bannerImage: String? = null,
    val chapters: Int? = null,
//    val character: CharacterModel? = null,
    val characterRole: Int? = null,
//    val characters: CharacterConnectionModel? = null,
//    val staff: StaffModel? = null,
//    val staffs: List<StaffModel>? = null,
    val staffRole: String? = null,
//    val staffs: StaffConnectionModel? = null,
    val countryOfOrigin: String? = null,
    val coverImage: MediaCoverImageModel? = null,
    val description: String? = null,
    val duration: Int? = null,
//    val endDate: FuzzyDateModel? = null,
    val episodes: Int? = null,
//    val externalLinks: List<MediaExternalLinkModel>? = null,
    val favourites: Int? = null,
    val format: MediaFormat? = null,
    val genres: List<String>? = null,
    val hashtag: String? = null,
    val idMal: Int = -1,
    val isAdult: Boolean = false,
    val isFavourite: Boolean = false,
    val meanScore: Int? = null,
//    val mediaListEntry: MediaListModel? = null,
//    val nextAiringEpisode: AiringScheduleModel? = null,
    val popularity: Int? = null,
//    val rankings: List<MediaRankModel>? = null,
//    val recommendations: RecommendationConnectionModel? = null,
//    val relations: MediaConnectionModel? = null,
//    val reviews: ReviewConnection? = null,
    val season: MediaSeason? = null,
    val seasonInt: Int? = null,
    val seasonYear: Int? = null,
    val siteUrl: String? = null,
    val source: Int? = null,
    val startDate: FuzzyDateModel? = null,
    val endDate: FuzzyDateModel? = null,
//    val stats: MediaStatsModel? = null,
    val status: MediaStatus? = null,
//    val streamingEpisodes: List<MediaStreamingEpisodeModel>? = null,
//    val studios: StudioConnectionModel? = null,
//    val studios: List<StudioModel>? = null,
    val synonyms: List<String>? = null,
//    val tags: List<MediaTagModel>? = null,
    val title: MediaTitleModel? = null,
//    val trailer: MediaTrailerModel? = null,
    val trending: Int? = null,
//    val trends: MediaTrendConnectionModel? = null,
    val type: MediaType? = null,
    val updatedAt: Int? = null,
    val volumes: Int? = null,
): BaseModel(id) {
    val isAnime get() = type == MediaType.ANIME
}


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
        bannerImage = bannerImage ?: coverImage?.mExtraLarge,
        isAdult = isAdult ?: false,
//    mediaListEntry = mediaListEntry?.let { list ->
//        MediaListModel().also { listModel ->
//            listModel.progress = list.progress ?: 0
//            listModel.status = list.status?.ordinal
//        }
//    }
    )
}

@StringRes
fun MediaFormat?.toStringRes(): Int {
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
        else -> R.string.unknown
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



@StringRes
fun MediaSort.toStringRes(): Int {
    return when (this) {
        MediaSort.ID -> R.string.id
        MediaSort.ID_DESC -> R.string.id_desc
        MediaSort.TITLE_ROMAJI -> R.string.title_romaji
        MediaSort.TITLE_ROMAJI_DESC -> R.string.title_romaji_desc
        MediaSort.TITLE_ENGLISH -> R.string.title_english
        MediaSort.TITLE_ENGLISH_DESC -> R.string.title_english_desc
        MediaSort.TITLE_NATIVE -> R.string.title_native
        MediaSort.TITLE_NATIVE_DESC -> R.string.title_native_desc
        MediaSort.TYPE -> R.string.type
        MediaSort.TYPE_DESC -> R.string.type_desc
        MediaSort.FORMAT -> R.string.format
        MediaSort.FORMAT_DESC -> R.string.format_desc
        MediaSort.START_DATE -> R.string.start_date
        MediaSort.START_DATE_DESC -> R.string.start_date_desc
        MediaSort.END_DATE -> R.string.end_date
        MediaSort.END_DATE_DESC -> R.string.end_date_desc
        MediaSort.SCORE -> R.string.score
        MediaSort.SCORE_DESC -> R.string.score_desc
        MediaSort.POPULARITY -> R.string.popularity
        MediaSort.POPULARITY_DESC -> R.string.popularity_desc
        MediaSort.TRENDING -> R.string.trending
        MediaSort.TRENDING_DESC -> R.string.trending_desc
        MediaSort.EPISODES -> R.string.episodes
        MediaSort.EPISODES_DESC -> R.string.episodes_desc
        MediaSort.DURATION -> R.string.duration
        MediaSort.DURATION_DESC -> R.string.duration_desc
        MediaSort.STATUS -> R.string.status
        MediaSort.STATUS_DESC -> R.string.status_desc
        MediaSort.CHAPTERS -> R.string.chapters
        MediaSort.CHAPTERS_DESC -> R.string.chapters_desc
        MediaSort.VOLUMES -> R.string.volumes
        MediaSort.VOLUMES_DESC -> R.string.volumes_desc
        MediaSort.UPDATED_AT -> R.string.updated_at
        MediaSort.UPDATED_AT_DESC -> R.string.updated_at_desc
        MediaSort.SEARCH_MATCH -> R.string.search_match
        MediaSort.FAVOURITES -> R.string.favourites
        MediaSort.FAVOURITES_DESC -> R.string.favourites_desc
        MediaSort.UNKNOWN__ -> R.string.unknown
    }
}

