package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import kotlinx.coroutines.flow.Flow

interface MediaListService {
    fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>>
}