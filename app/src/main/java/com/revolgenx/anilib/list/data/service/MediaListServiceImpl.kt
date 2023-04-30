package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MediaListServiceImpl(apolloRepository: ApolloRepository) : MediaListService,BaseService(apolloRepository) {
    override fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>> {
        return field.toQuery().map {
            /*TODO: add adult filters*/
            it.dataAssertNoErrors.mediaListCollection?.lists?.mapNotNull {
                it?.entries?.mapNotNull {
                    it?.takeIf { if (true/*field.canShowAdult*/) true else it.media?.isAdult == false }?.media?.id
                }
            }?.flatten() ?: emptyList()
        }.flowOn(Dispatchers.IO)
    }
}