package com.revolgenx.anilib.review.ui.model

import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ReviewRating
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel

data class ReviewModel(
    val id: Int = -1,
    val mediaId: Int = -1,
    val userId: Int = -1,
    val mediaType: MediaType? = null,
    val summary: String? = null,
    val body: String? = null,
    val rating: Int? = null,
    val ratingAmount: Int? = null,
    val userRating: ReviewRating? = null,
    val score: Int? = null,
    val private: Boolean = false,
    val siteUrl: String? = null,
    val createdAt: Int = 0,
    val updatedAt: Int = 0,
    val createdAtDate: String = "",
    val media: MediaModel? = null,
    val user: UserModel? = null,
) : BaseModel(id)


fun MediaReviewQuery.Node.toModel() = ReviewModel(
    id = id,
    rating = rating,
    ratingAmount = ratingAmount,
    summary = summary,
    userRating = userRating,
    user = user?.let {
        UserModel(
            id = it.id,
            avatar = it.avatar?.userAvatar?.toModel()
        )
    }
)

