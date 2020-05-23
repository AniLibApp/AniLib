package com.revolgenx.anilib.field.browse

import com.revolgenx.anilib.MediaSearchQuery
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.constant.CountryOfOrigins
import com.revolgenx.anilib.type.*

class MediaBrowseField : BrowseField() {

    override var type: Int = BrowseTypes.ANIME.ordinal
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

    override fun toQueryOrMutation(): Any {
        return MediaSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .apply {

                if (!query.isNullOrEmpty()) {
                    search(query)
                }

                season?.let {
                    season(MediaSeason.values()[it])
                }

                if (yearEnabled) {
                    if (isYearSame) {
                        minYear?.let {
                            seasonYear(it)
                        }
                    } else {
                        minYear?.let {
                            yearGreater(it * 10000)
                        }
                        maxYear?.let {
                            yearLesser(it * 10000)
                        }
                    }
                }

                when (type) {
                    BrowseTypes.ANIME.ordinal -> {
                        type(MediaType.ANIME)
                    }
                    BrowseTypes.MANGA.ordinal -> {
                        type(MediaType.MANGA)
                    }
                }

                format?.let {
                    format(MediaFormat.values()[it])
                }
                season?.let {
                    season(MediaSeason.values()[it])
                }
                status?.let {
                    status(MediaStatus.values()[it])
                }
                source?.let {
                    source(MediaSource.values()[it])
                }

                streamingOn?.takeIf { it.isNotEmpty() }?.let {
                    streamingOn(it)
                }

                genre?.takeIf { it.isNotEmpty() }?.let {
                    genre(it)
                }

                tags?.takeIf { it.isNotEmpty() }?.let {
                    tag(it)
                }

                sort?.let {
                    sort(listOf(MediaSort.values()[it]))
                }

                countryOfOrigin?.let {
                    country(CountryOfOrigins.values()[it].name)
                }

            }
            .build()
    }

}
