package com.revolgenx.anilib.home.recommendation.data.field

import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

enum class MediaRecommendationSort(val raw: String){
    NEWEST("ID_DESC"),
    HIGHEST_RATED("RATING_DESC"),
    LOWEST_RATED("RATING")
}

data class RecommendationField(
    var onList: Boolean = false,
    var sort: MediaRecommendationSort = MediaRecommendationSort.NEWEST
) : BaseSourceField<RecommendationQuery>() {

    override fun toQueryOrMutation(): RecommendationQuery {
        return RecommendationQuery(
            page = nn(page),
            perPage = nn(perPage),
            onList = nnBool(onList),
            sort = nn(listOf(RecommendationSort.safeValueOf(sort.raw)))
        )
    }
}
