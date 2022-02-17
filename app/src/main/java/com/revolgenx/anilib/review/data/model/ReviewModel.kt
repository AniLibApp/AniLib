package com.revolgenx.anilib.review.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.user.data.model.UserModel

open class ReviewModel : BaseModel() {
    var mediaId: Int = -1
    var userId: Int = -1
    var mediaType: Int? = null
    var summary: String? = null
    var body: String? = null
    var rating: Int? = null
    var ratingAmount: Int? = null
    var userRating: Int? = null
    var score: Int? = null
    var private: Boolean = false
    var siteUrl: String? = null
    var createdAt: Int = 0
    var updatedAt: Int = 0
    var createdAtDate: String = ""
    var media: MediaModel? = null
    var user: UserModel? = null
}
