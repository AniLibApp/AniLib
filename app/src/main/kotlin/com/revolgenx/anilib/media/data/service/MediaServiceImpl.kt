package com.revolgenx.anilib.media.data.service

import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationConnectionModel
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.ui.model.toModel
import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.media.data.field.MediaStatsField
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaStatsModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.model.toModel
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import com.revolgenx.anilib.staff.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaServiceImpl(apolloRepository: ApolloRepository) :
    MediaService, BaseService(apolloRepository) {
    override fun getMediaList(field: MediaField): Flow<PageModel<MediaModel>> =
        field.toQuery().map {
            it.dataAssertNoErrors.page.let {
                PageModel(
                    pageInfo = it.pageInfo.pageInfo,
                    data = it.media.mapNotNull { it?.media?.toModel() }
                )
            }
        }.onIO()

    override fun getMediaOverview(field: MediaOverviewField): Flow<MediaModel?> =
        field.toQuery().map { it.dataAssertNoErrors.media?.toModel() }.onIO()

    override fun getMediaRecommendationList(field: MediaRecommendationField): Flow<PageModel<RecommendationModel>> =
        field.toQuery().map {
            val recommendations = it.dataAssertNoErrors.media?.recommendations
            PageModel(
                pageInfo = recommendations?.pageInfo?.pageInfo,
                data = recommendations?.nodes?.mapNotNull { it?.recommendationFragment?.toModel() }
            )
        }.onIO()

    override fun getMediaCharacterList(field: MediaCharacterField): Flow<PageModel<CharacterEdgeModel>> =
        field.toQuery().map {
            val characters = it.dataAssertNoErrors.media?.characters
            PageModel(
                pageInfo = characters?.pageInfo?.pageInfo,
                data = characters?.edges?.mapNotNull { it?.toModel() }
            )
        }.onIO()


    override fun getMediaStaffList(field: MediaStaffField): Flow<PageModel<StaffEdgeModel>> {
        return field.toQuery().map {
            val staffs = it.dataAssertNoErrors.media?.staff
            PageModel(
                pageInfo = staffs?.pageInfo?.pageInfo,
                data = staffs?.edges?.mapNotNull { it?.toModel() }
            )
        }.onIO()
    }

    override fun getMediaReviewList(field: MediaReviewField): Flow<PageModel<ReviewModel>> {
        return field.toQuery().map {
            val reviews = it.dataAssertNoErrors.media?.reviews
            PageModel(
                pageInfo = reviews?.pageInfo?.pageInfo,
                data = reviews?.edges?.mapNotNull { it?.node?.toModel() }
            )
        }.onIO()
    }

    override fun getMediaStats(field: MediaStatsField): Flow<MediaStatsModel?> {
        return field.toQuery().map { it.dataAssertNoErrors.media?.toModel() }.onIO()
    }
}