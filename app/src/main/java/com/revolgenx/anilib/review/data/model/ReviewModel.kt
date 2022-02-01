package com.revolgenx.anilib.review.data.model

import com.revolgenx.anilib.user.data.model.UserPrefModel
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.constant.AlMediaType
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.user.data.model.UserModel

open class ReviewModel{
    var id: Int = -1
    var summary: String? = null
    var body: String = ""
    var userRating: Int? = null
    var rating: Int? = null
    var ratingAmount: Int? = null
    var score: Int? = null
    var private: Boolean = false
    var userPrefModel: UserPrefModel? = null
    var createdAtDate: String = ""
    var createdAt: Int = 0
    var media: MediaModel? = null
    var mediaId: Int = -1
    var mediaType: AlMediaType = AlMediaType.UNKNOWN
    var siteUrl: String? = null
    var updatedAt: Int = 0
    var user: UserModel? = null
    var userId: Int = -1

    //TODO
//    var userRating: ReviewRating? = ReviewRating.UNKNOWN
}
