package com.revolgenx.anilib.review.ui.model

import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.ReviewListQuery
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ReviewRating
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.Date

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
    val createdAtPrettyTime: String = "",
    val createdAtDate: String = "",
    val media: MediaModel? = null,
    val user: UserModel? = null,
) : BaseModel(id)


fun ReviewListQuery.Review.toModel(): ReviewModel {
    return ReviewModel(
        id = id,
        rating = rating,
        ratingAmount = ratingAmount,
        summary = summary,
        score = score,
        createdAt = createdAt,
        createdAtDate = createdAt.let {
            SimpleDateFormat.getDateInstance().format(Date(it * 1000L))
        },
        createdAtPrettyTime = createdAt.toLong().prettyTime(),
        userId = userId,
        user = user?.let {
            UserModel(
                id = it.id,
                name = it.name,
                avatar = it.avatar?.userAvatar?.toModel()
            )
        },
        mediaId = mediaId,
        media = media?.let {
            val coverImage = it.coverImage?.mediaCoverImage?.toModel()
            MediaModel(
                id = it.id,
                title = it.title?.mediaTitle?.toModel(),
                coverImage = coverImage,
                bannerImage = it.bannerImage ?: coverImage?.large,
                type = it.type,
                isAdult = it.isAdult ?: false,
            )
        }
    )
}


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

