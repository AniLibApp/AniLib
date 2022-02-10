package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.*
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.constant.CountryOfOrigins
import com.revolgenx.anilib.type.*

class SearchField : BaseSourceField<Any>() {
    var searchType = SearchTypes.ANIME
    var search: String? = null
    var season: Int? = null

    var yearGreater: Int? = null
    var yearLesser: Int? = null

    var year: Int? = null
    var status: Int? = null

    var episodesGreater: Int? = null
    var episodesLesser: Int? = null

    var chaptersGreater: Int? = null
    var chaptersLesser: Int? = null

    var volumesGreater: Int? = null
    var volumesLesser: Int? = null

    var durationGreater: Int? = null
    var durationLesser: Int? = null

    var isHentai: Boolean? = false

    var streamingOn: MutableList<String>? = null
    var readableOn: MutableList<String>? = null

    var countryOfOrigin: Int? = null
    var source: Int? = null
    var doujins: Boolean? = null
    var formatIn: MutableList<Int>? = null
    var genreIn: MutableList<String>? = null
    var genreNotIn: MutableList<String>? = null
    var tagsIn: MutableList<String>? = null
    var tagsNotIn: MutableList<String>? = null

    var sort: List<Int>? = null

    override fun toQueryOrMutation(): Any {
        return when (searchType) {
            SearchTypes.ANIME -> {
                MediaSearchQuery(
                    page = nn(page),
                    perPage = nn(perPage),
                    type = nn(MediaType.ANIME),
                    search = nn(search),
                    genre = nn(genreIn),
                    genreNotIn = nn(genreNotIn),
                    tag = nn(tagsIn),
                    tagNotIn = nn(tagsNotIn),
                    formatIn = nn(formatIn?.map { MediaFormat.values()[it] }),
                    isLicensed = nn(doujins?.takeIf { !it }),
                    isAdult = nn(isHentai),
                    episodesGreater = nn(episodesGreater),
                    episodesLesser = nn(episodesLesser),
                    durationGreater = nn(durationGreater),
                    durationLesser = nn(durationLesser),
                    licensedBy = nn(streamingOn),
                    yearGreater = nn(yearGreater),
                    yearLesser = nn(yearLesser),
                    season = nn(season?.let { MediaSeason.values()[it] }),
                    seasonYear = nn(year),
                    status = nn(status?.let { MediaStatus.values()[it] }),
                    country = nn(countryOfOrigin?.let { CountryOfOrigins.values()[it] }),
                    sort = nn(sort?.map { MediaSort.values()[it] }),
                    source = nn(source?.let { MediaSource.values()[it] })
                )
            }
            SearchTypes.MANGA -> {
                MediaSearchQuery(
                    page = nn(page),
                    perPage = nn(perPage),
                    type = nn(MediaType.MANGA),
                    search = nn(search),
                    genre = nn(genreIn),
                    genreNotIn = nn(genreNotIn),
                    tag = nn(tagsIn),
                    tagNotIn = nn(tagsNotIn),
                    formatIn = nn(formatIn?.map { MediaFormat.values()[it] }),
                    isLicensed = nn(doujins?.takeIf { !it }),
                    isAdult = nn(isHentai),
                    chaptersGreater = nn(chaptersGreater),
                    chaptersLesser = nn(chaptersLesser),
                    volumesGreater = nn(volumesGreater),
                    volumesLesser = nn(volumesLesser),
                    licensedBy = nn(readableOn),
                    yearGreater = nn(yearGreater),
                    yearLesser = nn(yearLesser),
                    seasonYear = nn(year),
                    status = nn(status?.let { MediaStatus.values()[it] }),
                    country = nn(countryOfOrigin?.let { CountryOfOrigins.values()[it] }),
                    sort = nn(sort?.map { MediaSort.values()[it] }),
                    source = nn(source?.let { MediaSource.values()[it] })
                )
            }
            SearchTypes.CHARACTER -> {
                CharacterSearchQuery(
                    page = nn(page),
                    perPage = nn(perPage),
                    search = nn(search)
                )
            }
            SearchTypes.STAFF -> {
                StaffSearchQuery(
                    page = nn(page),
                    perPage = nn(perPage),
                    search = nn(search)
                )
            }
            SearchTypes.STUDIO -> {
                StudioSearchQuery(
                    page = nn(page),
                    perPage = nn(perPage),
                    search = nn(search)
                )
            }
            SearchTypes.USER -> {
                UserSearchQuery(
                    page = nn(page),
                    perPage = nn(perPage),
                    search = nn(search),
                )
            }
        }
    }

}