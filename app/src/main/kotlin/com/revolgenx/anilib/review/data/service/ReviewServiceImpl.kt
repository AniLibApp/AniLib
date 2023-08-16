package com.revolgenx.anilib.review.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReviewServiceImpl(repository: ApolloRepository) : BaseService(repository), ReviewService {
    override fun getReviewList(field: ReviewListField): Flow<PageModel<ReviewModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.page.let { page ->
                PageModel(
                    pageInfo = page.pageInfo.pageInfo,
                    data = page.reviews?.mapNotNull { review ->
                        review.takeIf { true/*if (field.canShowAdult) true else review.media?.isAdult == false*/ /*todo filter 18 media*/ }
                            ?.toModel()
                    }
                )
            }
        }.onIO()
    }

    override fun getReview(field: ReviewField): Flow<ReviewModel?> {
        return field.toQuery().map { it.dataAssertNoErrors.review?.toModel() }.onIO()
    }
}