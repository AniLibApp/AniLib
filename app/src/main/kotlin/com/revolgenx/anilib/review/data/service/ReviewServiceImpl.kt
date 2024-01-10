package com.revolgenx.anilib.review.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.ReviewListField
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.model.toModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReviewServiceImpl(apolloRepository: ApolloRepository, mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore) : BaseService(apolloRepository, mediaSettingsPreferencesDataStore), ReviewService {
    override fun getReviewList(field: ReviewListField): Flow<PageModel<ReviewModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.page.let { page ->
                PageModel(
                    pageInfo = page.pageInfo.pageInfo,
                    data = page.reviews?.mapNotNull { review ->
                        review.takeIf { if (field.canShowAdult) true else review?.reviewFragment?.media?.isAdult == false}
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