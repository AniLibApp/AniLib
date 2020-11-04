package com.revolgenx.anilib.data.field.recommendation

import com.revolgenx.anilib.RecommendationQuery
import com.revolgenx.anilib.data.field.BaseSourceField
import com.revolgenx.anilib.type.RecommendationSort

class RecommendationField : BaseSourceField<RecommendationQuery>() {
    var onList: Boolean? = null
    var sort: Int? = null

    override fun toQueryOrMutation(): RecommendationQuery {
        return RecommendationQuery.builder()
            .page(page)
            .perPage(perPage)
            .apply {
                onList?.let {
                    onList(onList)
                }
                sort?.let {
                    when (it) {
                        0 -> {
                            sort(listOf(RecommendationSort.ID_DESC))
                        }
                        1 -> {
                            sort(listOf(RecommendationSort.RATING_DESC))
                        }
                        2 -> {
                            sort(listOf(RecommendationSort.RATING))
                        }
                        else -> {

                        }
                    }
                }
            }
            .build()
    }
}
