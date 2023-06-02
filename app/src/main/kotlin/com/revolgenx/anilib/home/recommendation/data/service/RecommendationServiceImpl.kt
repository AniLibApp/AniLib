package com.revolgenx.anilib.home.recommendation.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecommendationServiceImpl(repository: ApolloRepository) : BaseService(repository),
    RecommendationService {
    override fun getRecommendationList(field: RecommendationField): Flow<PageModel<RecommendationModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.page.let { page ->
                PageModel(
                    pageInfo = page.pageInfo.pageInfo,
                    data = page.recommendations?.mapNotNull { recommendation ->
                        recommendation?.takeIf {
                            /*    if (field.canShowAdult) true else ((it.media?.mediaContent?.isAdult == false)
                                and (it.mediaRecommendation?.mediaContent?.isAdult == false))*/
                            /*todo filter 18 media */
                            true
                        }?.toModel()
                    }
                )
            }
        }
    }
}