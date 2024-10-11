package com.revolgenx.anilib.airing.data.service

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.toModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.data.service.MediaListService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AiringScheduleServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore,
    private val mediaListService: MediaListService,
) : AiringScheduleService,
    BaseService(apolloRepository, appPreferencesDataStore) {

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
                    }.toQuery().mapData {
                        it.dataAssertNoErrors.page.let {
                            PageModel(
                                pageInfo = it.pageInfo.pageInfo,
                                data = it.airingSchedules?.mapNotNull {
                                    it?.takeIf { if (field.canShowAdult) true else it.media?.media?.isAdult == false }
                                        ?.toModel()
                                }
                            )
                        }
                    }.get()
                }
        } else {
            field.toQuery().mapData {
                it.dataAssertNoErrors.page.let {
                    PageModel(
                        pageInfo = it.pageInfo.pageInfo,
                        data = it.airingSchedules?.mapNotNull {
                            it?.takeIf { if (field.canShowAdult) true else it.media?.media?.isAdult == false }
                                ?.toModel()
                        }
                    )
                }
            }
        }
        return airingFlow
    }
}