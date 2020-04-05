package com.revolgenx.anilib.field.recommendation

import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

class RecommendationField : BaseSourceField<RecommendationQuery>() {
    var onList: Boolean = false
    var sort: List<RecommendationSort> = listOf(RecommendationSort.ID_DESC)

    override fun toQueryOrMutation(): RecommendationQuery {
        return RecommendationQuery.builder()
            .page(page)
            .perPage(perPage)
            .onList(onList)
            .sort(sort)
            .build()
    }
}
