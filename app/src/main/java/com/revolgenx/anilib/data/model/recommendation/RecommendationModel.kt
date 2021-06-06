package com.revolgenx.anilib.data.model.recommendation

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.CommonMediaModel

open class RecommendationModel : BaseModel() {
    var recommendationId: Int? = null
        set(value) {
            id = value
            field = value
        }

    var rating: Int? = null
    var userRating: Int? = null
    var recommendationFrom: CommonMediaModel? = null
    var recommended: CommonMediaModel? = null
}
