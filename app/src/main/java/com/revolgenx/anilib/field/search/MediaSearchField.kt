package com.revolgenx.anilib.field.search

import com.revolgenx.anilib.MediaSearchQuery
import com.revolgenx.anilib.constant.AdvanceSearchTypes

class MediaSearchField : BaseAdvanceSearchField() {

    override var type: Int = AdvanceSearchTypes.ANIME.ordinal
    var season: Int? = null
    var minYear: Int? = null
    var maxYear: Int? = null
    var yearEnabled = false
    var isYearSame = minYear == maxYear

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
            .search(query)
            .page(page)
            .perPage(perPage)
            .build()
    }
}
