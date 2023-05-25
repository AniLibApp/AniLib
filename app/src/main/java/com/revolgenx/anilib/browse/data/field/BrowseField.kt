package com.revolgenx.anilib.browse.data.field

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.BrowseQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.ui.model.FuzzyDateIntModel
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.common.ext.isNull
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.isManga
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaSource
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.type.MediaType

enum class BrowseTypes(@StringRes val title: Int) {
    ANIME(R.string.anime),
    MANGA(R.string.manga),
    CHARACTER(R.string.character),
    STAFF(R.string.staff),
    STUDIO(R.string.studio),
    USER(R.string.user)
}

val countryOfOrigins = listOf("JP", "KR", "CN", "TW")

data class BrowseField(
    var search: String? = null,
    var browseType: MutableState<BrowseTypes> = mutableStateOf(BrowseTypes.ANIME),
    var season: MediaSeason? = null,
    var yearGreater: FuzzyDateIntModel? = null,
    var yearLesser: FuzzyDateIntModel? = null,
    var year: Int? = null,
    var status: MediaStatus? = null,
    var episodesGreater: Int? = null,
    var episodesLesser: Int? = null,
    var chaptersGreater: Int? = null,
    var chaptersLesser: Int? = null,
    var volumesGreater: Int? = null,
    var volumesLesser: Int? = null,
    var durationGreater: Int? = null,
    var durationLesser: Int? = null,
    var isHentai: Boolean? = false,
    var streamingOn: MutableList<Int>? = null,
    var readableOn: MutableList<Int>? = null,
    var countryOfOrigin: Int? = null,
    var source: MediaSource? = null,
    var doujins: Boolean? = null,
    var format: MediaFormat? = null,
    var genreIn: MutableList<String>? = null,
    var genreNotIn: MutableList<String>? = null,
    var tagsIn: MutableList<String>? = null,
    var tagsNotIn: MutableList<String>? = null,
    var sort: MediaSort? = null,
    var minimumTagRank: Int? = null
) : BaseSourceField<BrowseQuery>() {
    val type get() = browseType.value
    override fun toQueryOrMutation(): BrowseQuery {
        val mediaType =
            if (browseType.value == BrowseTypes.ANIME) MediaType.ANIME else MediaType.MANGA
        return BrowseQuery(
            page = nn(page),
            perPage = nn(perPage),
            type = nn(mediaType),
            search = nn(search),
            genre = nn(genreIn),
            genreNotIn = nn(genreNotIn),
            tag = nn(tagsIn),
            tagNotIn = nn(tagsNotIn),
            format = nn(format),
            isLicensed = nn(doujins?.takeIf { !it }),
            isAdult = nn(isHentai),

            episodesGreater = nn(episodesGreater?.minus(1)?.takeIf { mediaType.isAnime() }),
            episodesLesser = nn(episodesLesser?.plus(1)?.takeIf { mediaType.isAnime() }),
            durationGreater = nn(durationGreater?.minus(1)?.takeIf { mediaType.isAnime() }),
            durationLesser = nn(durationLesser?.plus(1)?.takeIf { mediaType.isAnime() }),

            chaptersGreater = nn(chaptersGreater?.minus(1)?.takeIf { mediaType.isManga() }),
            chaptersLesser = nn(chaptersLesser?.plus(1)?.takeIf { mediaType.isManga() }),
            volumesGreater = nn(volumesGreater?.minus(1)?.takeIf { mediaType.isManga() }),
            volumesLesser = nn(volumesLesser?.plus(1)?.takeIf { mediaType.isManga() }),

            licensedByIdIn = nn(if (mediaType.isAnime()) streamingOn else readableOn),

            yearGreater = nn(yearGreater?.toFuzzyDateInt()?.minus(1)),
            yearLesser = nn(yearLesser?.toFuzzyDateInt()?.plus(10000)),

            season = nn(season?.takeIf { mediaType.isAnime() }),
            seasonYear = nn(season?.let { year }?.takeIf { mediaType.isAnime() }),
            year = nn(year?.takeIf { mediaType.isManga() || season.isNull() }?.let { "$it%" }),

            status = nn(status),
            country = nn(countryOfOrigin?.let { countryOfOrigins[it] }),
            sort = nn(sort?.let { listOf(it) }),
            source = nn(source),
            minimumTagRank = nn(minimumTagRank),

            browseMedia = type == BrowseTypes.ANIME || type == BrowseTypes.MANGA,
            browseCharacter = type == BrowseTypes.CHARACTER,
            browseStaff = type == BrowseTypes.STAFF,
            browseStudio = type == BrowseTypes.STUDIO,
            browseUser = type == BrowseTypes.USER
        )
    }
}