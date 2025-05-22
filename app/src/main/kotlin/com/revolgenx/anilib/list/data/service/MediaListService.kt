package com.revolgenx.anilib.list.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.field.MediaListCollectionIdField
import com.revolgenx.anilib.list.data.field.MediaListCompareField
import com.revolgenx.anilib.list.data.field.MediaListField
import com.revolgenx.anilib.list.ui.model.MediaListCollectionModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.Flow

interface MediaListService {
    fun getMediaListCollectionsIds(field: MediaListCollectionIdField): Flow<List<Int>>
    fun getMediaListCollection(field: MediaListCollectionField): Flow<MediaListCollectionModel>
    fun getMediaList(field: MediaListField): Flow<PageModel<MediaListModel>>
    fun getMediaListCompare(field: MediaListCompareField): Flow<PageModel<MediaListModel>>
}