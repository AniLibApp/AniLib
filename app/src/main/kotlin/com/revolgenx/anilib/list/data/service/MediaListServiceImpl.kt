package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.common.ext.onIO
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaListServiceImpl(apolloRepository: ApolloRepository) : MediaListService,
    BaseService(apolloRepository) {
    companion object {
        private val completedOrders = listOf(
            "Completed",
            "Completed TV",
            "Completed Movie",
            "Completed OVA",
            "Completed ONA",
            "Completed TV Short",
            "Completed Special",
            "Completed Music",
            "Completed Manga",
            "Completed Novel",
            "Completed One Shot"
        )
    }

    override fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>> {
        return field.toQuery().map {
            /*TODO: add adult filters*/
            it.dataAssertNoErrors.mediaListCollection?.lists?.mapNotNull { list ->
                list?.entries?.mapNotNull { entry ->
                    entry?.takeIf { if (true/*field.canShowAdult*/) true else it.media?.isAdult == false }?.media?.id
                }
            }?.flatten() ?: emptyList()
        }.onIO()
    }

    override fun getMediaListCollection(field: MediaListCollectionField): Flow<MediaListCollectionModel> {
        return field.toQuery().map { it.dataAssertNoErrors.toModel() }
    }
}