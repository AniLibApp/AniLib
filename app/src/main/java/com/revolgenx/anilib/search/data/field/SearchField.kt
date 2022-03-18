package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.*
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.common.data.model.FuzzyDateIntModel
import com.revolgenx.anilib.constant.CountryOfOrigins
import com.revolgenx.anilib.search.data.model.SearchFilterModel
import com.revolgenx.anilib.type.*

class SearchField : BaseSourceField<Any>() {
    override var perPage: Int = 30
    var search: String? = null
    var searchFilterModel = SearchFilterModel()

    override fun toQueryOrMutation(): Any {
        searchFilterModel.apply {
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

}