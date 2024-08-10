package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaListServiceImpl(
    apolloRepository: ApolloRepository,
    appPreferencesDataStore: AppPreferencesDataStore
) : MediaListService,
    BaseService(apolloRepository, appPreferencesDataStore) {

    override fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>> {
        return field.toQuery().map {
            it.dataAssertNoErrors.mediaListCollection?.lists?.mapNotNull { list ->
                list?.entries?.mapNotNull { entry ->
                    entry?.takeIf { if (field.canShowAdult) true else it.media?.isAdult == false }?.media?.id
                }
            }?.flatten().orEmpty()
        }
    }

    override fun getMediaListCollection(field: MediaListCollectionField): Flow<MediaListCollectionModel> {
        return field.toQuery().map { it.dataAssertNoErrors.toModel(field) }
    }
}