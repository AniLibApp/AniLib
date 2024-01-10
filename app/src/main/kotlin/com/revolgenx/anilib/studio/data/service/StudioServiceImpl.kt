package com.revolgenx.anilib.studio.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudioServiceImpl(apolloRepository: ApolloRepository, mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore) : BaseService(apolloRepository, mediaSettingsPreferencesDataStore),
    StudioService {
    override fun getStudioMedia(field: StudioField): Flow<PageModel<MediaModel>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.studio.let { studio ->
                PageModel(
                    pageInfo = studio?.media?.pageInfo?.pageInfo,
                    data = studio?.media?.nodes?.mapNotNull { media ->
                        media?.onMedia?.media?.toModel()?.also { mediaModel ->
                            mediaModel.studio = studio.toModel()
                        }
                    }
                )
            }
        }.onIO()
    }
}