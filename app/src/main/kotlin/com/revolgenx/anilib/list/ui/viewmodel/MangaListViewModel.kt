package com.revolgenx.anilib.list.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AppDataStore
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.type.MediaType

class MangaListViewModel(
    mediaListService: MediaListService,
    appDataStore: AppDataStore,
    mediaListFilterDataStore: MediaListFilterDataStore
) : MediaListViewModel(mediaListService, appDataStore, mediaListFilterDataStore) {
    override val field: MediaListCollectionField = MediaListCollectionField(MediaType.MANGA)
}