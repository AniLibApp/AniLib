package com.revolgenx.anilib.home.recommendation.data.field

import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

data class RecommendationField(
    var onList: Boolean? = null,
    var sort: RecommendationSort = RecommendationSort.ID_DESC
) : BaseSourceField<RecommendationQuery>() {

    override fun toQueryOrMutation(): RecommendationQuery {
        return RecommendationQuery(
            page = nn(page),
            perPage = nn(perPage),
            onList = nn(onList),
            sort = nn(listOf(sort))
        )
    }
}
