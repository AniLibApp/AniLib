package com.revolgenx.anilib.field.home.airing

import com.revolgenx.anilib.AiringScheduleQuery
import com.revolgenx.anilib.field.BaseSourceField
import com.revolgenx.anilib.type.AiringSort

class AiringMediaField : BaseSourceField<AiringScheduleQuery>() {
    var notYetAired = true
    var airingGreaterThan: Int? = null
    var airingLessThan: Int? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): AiringScheduleQuery {
        return AiringScheduleQuery.builder()
            .page(page)
            .perPage(perPage)
            .notYetAired(notYetAired)
            .apply {
                airingGreaterThan?.let {
                    airingAtGreater(it)
                }
                airingLessThan?.let {
                    airingLessThan
                }
                sort?.let {
                    sort(listOf(AiringSort.values()[it]))
                }
            }
            .build()
    }
}
