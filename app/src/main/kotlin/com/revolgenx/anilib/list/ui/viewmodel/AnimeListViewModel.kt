package com.revolgenx.anilib.list.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.type.MediaType

class AnimeListViewModel(
    mediaListService: MediaListService,
    authPreferencesDataStore: AuthPreferencesDataStore,
    mediaListFilterDataStore: MediaListFilterDataStore
) : MediaListViewModel(mediaListService, authPreferencesDataStore, mediaListFilterDataStore) {
    override val field: MediaListCollectionField = MediaListCollectionField(MediaType.ANIME)
}