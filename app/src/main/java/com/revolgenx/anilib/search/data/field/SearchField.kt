package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.*
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.common.data.model.FuzzyDateIntModel
import com.revolgenx.anilib.constant.CountryOfOrigins
import com.revolgenx.anilib.type.*

class SearchField : BaseSourceField<Any>() {
    override var perPage: Int = 30
    var searchType = SearchTypes.ANIME
    var search: String? = null
    var season: Int? = null

    var yearGreater: FuzzyDateIntModel? = null
    var yearLesser: FuzzyDateIntModel? = null

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
    var format:Int? = null
    var genreIn: MutableList<String>? = null
    var genreNotIn: MutableList<String>? = null
    var tagsIn: MutableList<String>? = null
    var tagsNotIn: MutableList<String>? = null

    var sort: Int? = null
    var minimumTagRank:Int? = null

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
                    format = nn(format?.let { MediaFormat.values()[it] }),
                    isLicensed = nn(doujins?.takeIf { it }?.not()),
                    isAdult = nn(isHentai),
                    episodesGreater = nn(episodesGreater),
                    episodesLesser = nn(episodesLesser),
                    durationGreater = nn(durationGreater),
                    durationLesser = nn(durationLesser),
                    licensedBy = nn(streamingOn),
                    yearGreater = nn(yearGreater?.toFuzzyDateInt()),
                    yearLesser = nn(yearLesser?.toFuzzyDateInt()),
                    season = nn(season?.let { MediaSeason.values()[it] }),
                    year = nn(year?.let { "$it%" }),
                    status = nn(status?.let { MediaStatus.values()[it] }),
                    country = nn(countryOfOrigin?.let { CountryOfOrigins.values()[it].name }),
                    sort = nn(sort?.let { listOf(MediaSort.values()[it]) }),
                    source = nn(source?.let { MediaSource.values()[it] }),
                    minimumTagRank = nn(minimumTagRank)
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
                    format = nn(format?.let { MediaFormat.values()[it] }),
                    isLicensed = nn(doujins?.takeIf { !it }),
                    isAdult = nn(isHentai),
                    chaptersGreater = nn(chaptersGreater),
                    chaptersLesser = nn(chaptersLesser),
                    volumesGreater = nn(volumesGreater),
                    volumesLesser = nn(volumesLesser),
                    licensedBy = nn(readableOn),
                    year = nn(year?.let { "$it%" }),
                    yearGreater = nn(yearGreater?.toFuzzyDateInt()),
                    yearLesser = nn(yearLesser?.toFuzzyDateInt()),
                    seasonYear = nn(year),
                    status = nn(status?.let { MediaStatus.values()[it] }),
                    country = nn(countryOfOrigin?.let { CountryOfOrigins.values()[it].name }),
                    sort = nn(sort?.let { listOf(MediaSort.values()[it]) }),
                    source = nn(source?.let { MediaSource.values()[it] }),
                    minimumTagRank = nn(minimumTagRank)
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