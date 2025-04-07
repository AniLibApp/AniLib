package com.revolgenx.anilib.studio.data.service

import com.revolgenx.anilib.common.data.field.ToggleFavoriteField
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.ui.model.toModel
import kotlinx.coroutines.flow.Flow

class StudioServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore,
    private val toggleService: ToggleService
) : BaseService(apolloRepository, appPreferencesDataStore),
    StudioService {
    override fun getStudioMedia(field: StudioField): Flow<PageModel<MediaModel>> {
        return field.toQuery().mapData {
            it.dataAssertNoErrors.studio.let { studio ->
                val studioModel = studio?.toModel()
                PageModel(
                    pageInfo = studio?.media?.pageInfo?.pageInfo,
                    data = studio?.media?.nodes?.mapNotNull { media ->
                        media?.onMedia?.media?.takeIf {
                            if (field.canShowAdult) true else it.isAdult == false
                        }?.toModel()?.also { mediaModel ->
                            mediaModel.studio = studioModel
                        }
                    }
                )
            }
        }
    }

    override fun toggleFavorite(studioId: Int): Flow<Boolean> {
        return toggleService.toggleFavourite(ToggleFavoriteField(studioId = studioId))
    }
}