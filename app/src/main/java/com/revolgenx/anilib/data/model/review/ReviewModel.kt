package com.revolgenx.anilib.data.model.review

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.user.UserPrefModel
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.markwon.MarkdownModel

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
