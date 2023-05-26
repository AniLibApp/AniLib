package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import kotlinx.coroutines.flow.Flow

interface MediaListService {
    fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>>
    fun getMediaListCollection(field: MediaListCollectionField): Flow<MediaListCollectionModel>
}