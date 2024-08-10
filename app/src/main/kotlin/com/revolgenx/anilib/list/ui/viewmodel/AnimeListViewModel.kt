package com.revolgenx.anilib.list.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.type.MediaType

class AnimeListViewModel(
    mediaListService: MediaListService,
    mediaListEntryService: MediaListEntryService,
    appPreferencesDataStore: AppPreferencesDataStore,
    mediaListFilterDataStore: MediaListFilterDataStore,
) : MediaListViewModel(
    mediaListService,
    mediaListEntryService,
    appPreferencesDataStore,
    mediaListFilterDataStore,
) {
    override val field: MediaListCollectionField = MediaListCollectionField(MediaType.ANIME)
}