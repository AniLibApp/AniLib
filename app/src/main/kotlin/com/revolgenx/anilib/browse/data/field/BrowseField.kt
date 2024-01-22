package com.revolgenx.anilib.browse.data.field

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.BrowseQuery
import com.revolgenx.anilib.browse.data.store.BrowseFilterData
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
import anilib.i18n.R as I18nR

enum class BrowseTypes(@StringRes val title: Int) {
    ANIME(I18nR.string.anime),
    MANGA(I18nR.string.manga),
    CHARACTER(I18nR.string.character),
    STAFF(I18nR.string.staff),
    STUDIO(I18nR.string.studio),
    USER(I18nR.string.user)
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
    var streamingOn: List<Int>? = null,
    var readableOn: List<Int>? = null,
    var countryOfOrigin: Int? = null,
    var source: MediaSource? = null,
    var doujins: Boolean? = null,
    var format: MediaFormat? = null,
    var formatsIn: List<MediaFormat>? = null,
    var genreIn: List<String>? = null,
    var genreNotIn: List<String>? = null,
    var tagsIn: List<String>? = null,
    var tagsNotIn: List<String>? = null,
    var sort: MediaSort? = null,
    var minimumTagRank: Int? = null
) : BaseSourceField<BrowseQuery>() {
    override var perPage: Int = 30
    val type get() = browseType.value
    override fun toQueryOrMutation(): BrowseQuery {
        val mediaType =
            if (browseType.value == BrowseTypes.ANIME) MediaType.ANIME else MediaType.MANGA
        return BrowseQuery(
            page = nn(page),
            perPage = nn(perPage),
            type = nn(mediaType),
            search = nnString(search),
            genre = nn(genreIn),
            genreNotIn = nn(genreNotIn),
            tagIn = nn(tagsIn),
            tagNotIn = nn(tagsNotIn),
            formatsIn = nn(formatsIn),
            isLicensed = nn(doujins?.takeIf { !it }),
            isAdult = nn(isHentai),

            episodesGreater = nn(episodesGreater?.minus(1)?.takeIf { mediaType.isAnime }),
            episodesLesser = nn(episodesLesser?.plus(1)?.takeIf { mediaType.isAnime }),
            durationGreater = nn(durationGreater?.minus(1)?.takeIf { mediaType.isAnime }),
            durationLesser = nn(durationLesser?.plus(1)?.takeIf { mediaType.isAnime }),

            chaptersGreater = nn(chaptersGreater?.minus(1)?.takeIf { mediaType.isManga }),
            chaptersLesser = nn(chaptersLesser?.plus(1)?.takeIf { mediaType.isManga }),
            volumesGreater = nn(volumesGreater?.minus(1)?.takeIf { mediaType.isManga }),
            volumesLesser = nn(volumesLesser?.plus(1)?.takeIf { mediaType.isManga }),

            licensedByIdIn = nn(if (mediaType.isAnime) streamingOn else readableOn),

            yearGreater = nn(yearGreater?.toFuzzyDateInt()?.minus(1)),
            yearLesser = nn(yearLesser?.toFuzzyDateInt()?.plus(10000)),

            season = nn(season?.takeIf { mediaType.isAnime }),
            seasonYear = nn(season?.let { year }?.takeIf { mediaType.isAnime }),
            year = nnString(year?.takeIf { mediaType.isManga || season.isNull() }?.let { "$it%" }),

            status = nn(status),
            country = nnString(countryOfOrigin?.let { countryOfOrigins[it] }),
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




    fun toBrowseFilter(): BrowseFilterData {
        return BrowseFilterData(
            browseType = browseType.value,
            season = season,
            yearGreater = yearGreater,
            yearLesser = yearLesser,
            year = year,
            status = status,
            episodesGreater = episodesGreater,
            episodesLesser = episodesLesser,
            chaptersGreater = chaptersGreater,
            chaptersLesser = chaptersLesser,
            volumesGreater = volumesGreater,
            volumesLesser = volumesLesser,
            durationGreater = durationGreater,
            durationLesser = durationLesser,
            isHentai = isHentai,
            streamingOn = streamingOn,
            readableOn = readableOn,
            countryOfOrigin = countryOfOrigin,
            source = source,
            doujins = doujins,
            format = format,
            formatsIn = formatsIn,
            genreIn = genreIn,
            genreNotIn = genreNotIn,
            tagsIn = tagsIn,
            tagsNotIn = tagsNotIn,
            sort = sort,
            minimumTagRank = minimumTagRank,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BrowseField

        if (search != other.search) return false
        if (browseType != other.browseType) return false
        if (season != other.season) return false
        if (yearGreater != other.yearGreater) return false
        if (yearLesser != other.yearLesser) return false
        if (year != other.year) return false
        if (status != other.status) return false
        if (episodesGreater != other.episodesGreater) return false
        if (episodesLesser != other.episodesLesser) return false
        if (chaptersGreater != other.chaptersGreater) return false
        if (chaptersLesser != other.chaptersLesser) return false
        if (volumesGreater != other.volumesGreater) return false
        if (volumesLesser != other.volumesLesser) return false
        if (durationGreater != other.durationGreater) return false
        if (durationLesser != other.durationLesser) return false
        if (isHentai != other.isHentai) return false
        if (streamingOn != other.streamingOn) return false
        if (readableOn != other.readableOn) return false
        if (countryOfOrigin != other.countryOfOrigin) return false
        if (source != other.source) return false
        if (doujins != other.doujins) return false
        if (format != other.format) return false
        if (formatsIn != other.formatsIn) return false
        if (genreIn != other.genreIn) return false
        if (genreNotIn != other.genreNotIn) return false
        if (tagsIn != other.tagsIn) return false
        if (tagsNotIn != other.tagsNotIn) return false
        if (sort != other.sort) return false
        if (minimumTagRank != other.minimumTagRank) return false

        return true
    }

    override fun hashCode(): Int {
        var result = search?.hashCode() ?: 0
        result = 31 * result + browseType.hashCode()
        result = 31 * result + (season?.hashCode() ?: 0)
        result = 31 * result + (yearGreater?.hashCode() ?: 0)
        result = 31 * result + (yearLesser?.hashCode() ?: 0)
        result = 31 * result + (year ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (episodesGreater ?: 0)
        result = 31 * result + (episodesLesser ?: 0)
        result = 31 * result + (chaptersGreater ?: 0)
        result = 31 * result + (chaptersLesser ?: 0)
        result = 31 * result + (volumesGreater ?: 0)
        result = 31 * result + (volumesLesser ?: 0)
        result = 31 * result + (durationGreater ?: 0)
        result = 31 * result + (durationLesser ?: 0)
        result = 31 * result + (isHentai?.hashCode() ?: 0)
        result = 31 * result + (streamingOn?.hashCode() ?: 0)
        result = 31 * result + (readableOn?.hashCode() ?: 0)
        result = 31 * result + (countryOfOrigin ?: 0)
        result = 31 * result + (source?.hashCode() ?: 0)
        result = 31 * result + (doujins?.hashCode() ?: 0)
        result = 31 * result + (format?.hashCode() ?: 0)
        result = 31 * result + (formatsIn?.hashCode() ?: 0)
        result = 31 * result + (genreIn?.hashCode() ?: 0)
        result = 31 * result + (genreNotIn?.hashCode() ?: 0)
        result = 31 * result + (tagsIn?.hashCode() ?: 0)
        result = 31 * result + (tagsNotIn?.hashCode() ?: 0)
        result = 31 * result + (sort?.hashCode() ?: 0)
        result = 31 * result + (minimumTagRank ?: 0)
        result = 31 * result + perPage
        return result
    }
}