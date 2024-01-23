package com.revolgenx.anilib.airing.data.service

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class AiringScheduleServiceImpl(
    apolloRepository: ApolloRepository,
    mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore,
    private val mediaListService: MediaListService,
) : AiringScheduleService,
    BaseService(apolloRepository, mediaSettingsPreferencesDataStore) {

    override fun getAiringSchedule(field: AiringScheduleField): Flow<PageModel<AiringScheduleModel>> {
        val airingFlow = if (field.needMediaListData) {
            val collectionIdField =
                MediaListCollectionIdField(mediaListStatus = field.mediaListStatus).also { f ->
                    f.userId = field.userId
                }
            mediaListService.getMediaListCollectionsIds(collectionIdField)
                .map { t ->
                    field.also {
                        it.needMediaListData = false
                        it.mediaListIds = t
                    }.toQuery().map {
                        it.dataAssertNoErrors.page.let {
                            PageModel(
                                pageInfo = it.pageInfo.pageInfo,
                                data = it.airingSchedules?.mapNotNull { it?.toModel() }
                            )
                        }
                    }.get()
                }
        } else {
            field.toQuery().map {
                it.dataAssertNoErrors.page.let {
                    PageModel(
                        pageInfo = it.pageInfo.pageInfo,
                        data = it.airingSchedules?.mapNotNull { it?.toModel() }
                    )
                }
            }
        }
        return airingFlow
    }
}