package com.revolgenx.anilib.review.ui.model

import android.text.Spanned
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.MediaReviewQuery
import com.revolgenx.anilib.ReviewListQuery
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.prettyTime
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.fragment.ReviewFragment
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ReviewRating
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.toModel
import java.text.SimpleDateFormat
import java.util.Date

data class ReviewModel(
    val id: Int = -1,
    val mediaId: Int = -1,
    val userId: Int = -1,
    val mediaType: MediaType? = null,
    val summary: String? = null,
    val body: String? = null,
    val score: Int? = null,
    val private: Boolean = false,
    val siteUrl: String? = null,
    val createdAt: Int = 0,
    val updatedAt: Int = 0,
    val createdAtPrettyTime: String = "",
    val createdAtDate: String = "",
    val media: MediaModel? = null,
    val user: UserModel? = null,
    val anilifiedBody: String = "",
    val bodySpanned: Spanned? = null,
    var rating: MutableIntState = mutableIntStateOf(0),
    val ratingAmount: MutableIntState = mutableIntStateOf(0),
    var userRating: MutableState<ReviewRating> = mutableStateOf(ReviewRating.NO_VOTE)
) : BaseModel

data class RateReviewModel(
    val id: Int,
    val userRating: ReviewRating,
    val ratingAmount: Int,
    val rating: Int
)

fun ReviewListQuery.Review.toModel(): ReviewModel {
    return reviewFragment.toModel()
}

fun ReviewQuery.Review.toModel(): ReviewModel {
    val anilifiedBody = anilify(body)
    return reviewFragment.toModel().copy(
        body = body,
        anilifiedBody = anilifiedBody,
        bodySpanned = markdown.toMarkdown(anilifiedBody),
        private = this.private ?: false,
        siteUrl = siteUrl,
    ).also {
        it.userRating.value = userRating ?: ReviewRating.NO_VOTE
    }
}

fun MediaReviewQuery.Node.toModel() = ReviewModel(
    id = id,
    rating = mutableIntStateOf(rating.orZero()),
    ratingAmount = mutableIntStateOf(ratingAmount.orZero()) ,
    summary = summary,
    user = user?.let {
        UserModel(
            id = it.id,
            avatar = it.avatar?.userAvatar?.toModel()
        )
    }
).also {
    it.userRating.value = userRating ?: ReviewRating.NO_VOTE
}

fun ReviewFragment.toModel(): ReviewModel {
    return ReviewModel(
        id = id,
        rating = mutableIntStateOf(rating.orZero()),
        ratingAmount = mutableIntStateOf(ratingAmount.orZero()),
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

