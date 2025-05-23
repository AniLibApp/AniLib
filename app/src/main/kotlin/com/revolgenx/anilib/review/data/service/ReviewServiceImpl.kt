package com.revolgenx.anilib.review.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.review.data.field.DeleteReviewField
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.data.field.SaveReviewField
import com.revolgenx.anilib.review.ui.model.RateReviewModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.model.toModel
import com.revolgenx.anilib.type.ReviewRating
import kotlinx.coroutines.flow.Flow

class ReviewServiceImpl(apolloRepository: ApolloRepository, appPreferencesDataStore: AppPreferencesDataStore) : BaseService(apolloRepository, appPreferencesDataStore), ReviewService {
    override fun getReviewList(field: ReviewListField): Flow<PageModel<ReviewModel>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.page.let { page ->
                PageModel(
                    pageInfo = page.pageInfo.pageInfo,
                    data = page.reviews?.mapNotNull { review ->
                        review.takeIf { if (field.canShowAdult) true else review?.reviewFragment?.media?.isAdult == false}
                            ?.toModel()
                    }
                )
            }
        }
    }

    override fun getReview(field: ReviewField): Flow<ReviewModel?> {
        return field.toQuery().mapData { it.dataAssertNoErrors.review?.toModel() }
    }

    override fun rateReview(field: RateReviewField): Flow<RateReviewModel?> {
        return field.toMutation().mapData {
            it.dataAssertNoErrors.rateReview?.let { review ->
                RateReviewModel(
                    id = review.id,
                    userRating =  review.userRating ?: ReviewRating.NO_VOTE,
                    ratingAmount =  review.ratingAmount.orZero(),
                    rating = review.rating.orZero()
                )
            }
        }
    }

    override fun saveReview(field: SaveReviewField): Flow<Int> {
        return field.toMutation().mapData { it.dataAssertNoErrors.saveReview?.id!! }
    }

    override fun deleteReview(id: Int): Flow<Boolean> {
        return DeleteReviewField(id).toMutation().mapData { it.dataAssertNoErrors.deleteReview?.deleted == true }
    }
}