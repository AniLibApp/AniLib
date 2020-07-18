package com.revolgenx.anilib.model.review

import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.UserPrefModel
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.markwon.MarkdownModel

open class ReviewModel : BaseModel() {
    var reviewId: Int? = null
    var summary: String? = null
    var body: MarkdownModel = MarkdownModel()
    var userRating: Int? = null
    var rating: Int? = null
    var ratingAmount: Int? = null
    var score: Int? = null
    var private: Boolean? = null
    var userPrefModel: UserPrefModel? = null
    var mediaModel: CommonMediaModel? = null
    var createdAt: String? = null
}
