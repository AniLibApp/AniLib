package com.revolgenx.anilib.search.data.model

import com.revolgenx.anilib.common.data.model.FuzzyDateIntModel
import com.revolgenx.anilib.search.data.field.SearchTypes

data class SearchFilterModel(
    var searchType: SearchTypes = SearchTypes.ANIME,
    var season: Int? = null,
    var yearGreater: FuzzyDateIntModel? = null,
    var yearLesser: FuzzyDateIntModel? = null,
    var year: Int? = null,
    var status: Int? = null,
    var episodesGreater: Int? = null,
    var episodesLesser: Int? = null,
    var chaptersGreater: Int? = null,
    var chaptersLesser: Int? = null,
    var volumesGreater: Int? = null,
    var volumesLesser: Int? = null,
    var durationGreater: Int? = null,
    var durationLesser: Int? = null,
    var isHentai: Boolean? = false,
    var streamingOn: MutableList<String>? = null,
    var readableOn: MutableList<String>? = null,
    var countryOfOrigin: Int? = null,
    var source: Int? = null,
    var doujins: Boolean? = null,
    var format: Int? = null,
    var genreIn: MutableList<String>? = null,
    var genreNotIn: MutableList<String>? = null,
    var tagsIn: MutableList<String>? = null,
    var tagsNotIn: MutableList<String>? = null,
    var sort: Int? = null,
    var minimumTagRank: Int? = null
){

}