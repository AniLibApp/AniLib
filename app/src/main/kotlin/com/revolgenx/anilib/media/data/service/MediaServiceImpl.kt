package com.revolgenx.anilib.media.data.service

import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.character.ui.model.toModel
import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.logException
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.ui.model.toModel
import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.media.data.field.MediaSocialFollowingField
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.media.data.field.MediaStatsField
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaSocialFollowingModel
import com.revolgenx.anilib.media.ui.model.MediaStatsModel
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.model.toModel
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import com.revolgenx.anilib.staff.ui.model.toModel
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.flow.Flow

class MediaServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore,
    private val toggleService: ToggleService
) :
    MediaService, BaseService(apolloRepository, appPreferencesDataStore) {
    override fun getMediaList(field: MediaField): Flow<PageModel<MediaModel>> =
        field.toQuery().mapData {
            it.dataAssertNoErrors.page.let {
                PageModel(
                    pageInfo = it.pageInfo.pageInfo,
                    data = it.media.mapNotNull { it?.media?.toModel() }
                )
            }
        }.logException()

    override fun getMediaOverview(field: MediaOverviewField): Flow<MediaModel?> =
        field.toQuery().mapData { it.dataAssertNoErrors.media?.toModel() }

    override fun getMediaRecommendationList(field: MediaRecommendationField): Flow<PageModel<RecommendationModel>> =
        field.toQuery().mapData {
            val recommendations = it.dataAssertNoErrors.media?.recommendations
            PageModel(
                pageInfo = recommendations?.pageInfo?.pageInfo,
                data = recommendations?.nodes?.mapNotNull { it?.recommendationFragment?.toModel() }
            )
        }

    override fun getMediaCharacterList(field: MediaCharacterField): Flow<PageModel<CharacterEdgeModel>> =
        field.toQuery().mapData {
            val characters = it.dataAssertNoErrors.media?.characters
            PageModel(
                pageInfo = characters?.pageInfo?.pageInfo,
                data = characters?.edges?.mapNotNull { it?.toModel() }
            )
        }


    override fun getMediaStaffList(field: MediaStaffField): Flow<PageModel<StaffEdgeModel>> {
        return field.toQuery().mapData {
            val staffs = it.dataAssertNoErrors.media?.staff
            PageModel(
                pageInfo = staffs?.pageInfo?.pageInfo,
                data = staffs?.edges?.mapNotNull { it?.toModel() }
            )
        }
    }

    override fun getMediaReviewList(field: MediaReviewField): Flow<PageModel<ReviewModel>> {
        return field.toQuery().mapData {
            val reviews = it.dataAssertNoErrors.media?.reviews
            PageModel(
                pageInfo = reviews?.pageInfo?.pageInfo,
                data = reviews?.edges?.mapNotNull { it?.node?.toModel() }
            )
        }
    }

    override fun getMediaStats(field: MediaStatsField): Flow<MediaStatsModel?> {
        return field.toQuery().mapData { it.dataAssertNoErrors.media?.toModel() }
    }

    override fun toggleFavourite(mediaId: Int, type: MediaType): Flow<Boolean> {
        val isAnime = type.isAnime
        return toggleService.toggleFavourite(
            ToggleFavoriteField(
                animeId = if (isAnime) mediaId else null,
                mangaId = if (isAnime) null else mediaId
            )
        )
    }

    override fun getMediaSocialFollowingList(field: MediaSocialFollowingField): Flow<PageModel<MediaSocialFollowingModel>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.page.let { page ->
                PageModel(
                    pageInfo = page.pageInfo.pageInfo,
                    data = page.mediaList?.mapNotNull { it?.toModel() }
                )
            }
        }
    }
}