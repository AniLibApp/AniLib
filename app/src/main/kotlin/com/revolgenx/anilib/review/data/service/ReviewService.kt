package com.revolgenx.anilib.review.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.data.field.SaveReviewField
import com.revolgenx.anilib.review.ui.model.RateReviewModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface ReviewService {
    fun getReviewList(field: ReviewListField): Flow<PageModel<ReviewModel>>
    fun getReview(field: ReviewField): Flow<ReviewModel?>
    fun rateReview(field: RateReviewField): Flow<RateReviewModel?>
    fun saveReview(field: SaveReviewField): Flow<Int>
    fun deleteReview(id: Int): Flow<Boolean>
}
