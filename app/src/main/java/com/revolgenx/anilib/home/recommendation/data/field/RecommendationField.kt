package com.revolgenx.anilib.home.recommendation.data.field

import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

class RecommendationField : BaseSourceField<RecommendationQuery>() {
    var onList: Boolean? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): RecommendationQuery {
        val mediaSort = sort?.let {
            when (it) {
                0 -> {
                    listOf(RecommendationSort.ID_DESC)
                }
                1 -> {
                    listOf(RecommendationSort.RATING_DESC)
                }
                2 -> {
                    listOf(RecommendationSort.RATING)
                }
                else -> {
                    null
                }
            }
        }
        return RecommendationQuery(
            page = nn(page),
            perPage = nn(perPage),
            onList = nn(onList),
            sort = nn(mediaSort)
        )
    }
}
