package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.MediaSearchQuery
import com.revolgenx.anilib.common.preference.userEnabledAdultContent
import com.revolgenx.anilib.constant.CountryOfOrigins
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.type.*

class MediaSearchField : SearchField() {

    override var type: Int = SearchTypes.ANIME.ordinal
    var season: Int? = null
    var minYear: Int? = null
    var maxYear: Int? = null
    var yearEnabled = false
    val isYearSame
        get() = minYear == maxYear
    var sort: Int? = null

    var format: Int? = null
    var status: Int? = null
    var streamingOn: List<String>? = null
    var countryOfOrigin: Int? = null
    var source: Int? = null
    var genre: List<String>? = null
    var tags: List<String>? = null
    var tagsNotIn: List<String>? = null
    var genreNotIn: List<String>? = null
    var hentaiOnly: Boolean? = null

    override fun toQueryOrMutation(): Any {
        val mSeason = season?.let {
            MediaSeason.values()[it]
        }

        var seasonYear: Int? = null
        var yearGreater: Int? = null
        var yearLesser: Int? = null

        if (yearEnabled) {
            if (isYearSame) {
                seasonYear = minYear
            } else {
                yearGreater = minYear?.let {
                    it * 10000
                }
                yearLesser = maxYear?.let {
                    it * 10000
                }
            }
        }


        val mType = when (type) {
            SearchTypes.ANIME.ordinal -> {
                MediaType.ANIME
            }
            SearchTypes.MANGA.ordinal -> {
                MediaType.MANGA
            }
            else -> null
        }

        val mFormat = format?.let {
            MediaFormat.values()[it]
        }

        val mStatus = status?.let {
            MediaStatus.values()[it]
        }
        val mSource = source?.let {
            MediaSource.values()[it]
        }

        val mSort = sort?.let {
            listOf(MediaSort.values()[it])
        }

        val cOO = countryOfOrigin?.let {
            CountryOfOrigins.values()[it].name
        }

        var isAdult: Boolean? = null

        if (userEnabledAdultContent(context)) {
            if (!canShowAdult) {
                isAdult = canShowAdult
            }
            if (hentaiOnly == true && canShowAdult) {
                isAdult = hentaiOnly
            } else if (hentaiOnly == false) {
                isAdult = false
            }

        } else {
            isAdult = canShowAdult
        }

        return MediaSearchQuery(
            page = nn(page),
            perPage = nn(perPage),
            search = nn(query),
            season = nn(mSeason),
            seasonYear = nn(seasonYear),
            yearGreater = nn(yearGreater),
            yearLesser = nn(yearLesser),
            type = nn(mType),
            source = nn(mSource),
            tag = nn(tags),
            tagNotIn = nn(tagsNotIn),
            genreNotIn = nn(genreNotIn),
            genre = nn(genre),
            sort = nn(mSort),
            streamingOn = nn(streamingOn),
            country = nn(cOO),
            isAdult = nn(isAdult),
            status = nn(mStatus),
            format = nn(mFormat)
        )
    }
}
