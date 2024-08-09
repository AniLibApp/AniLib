package com.revolgenx.anilib.list.ui.viewmodel

import com.revolgenx.anilib.common.data.store.AuthPreferencesDataStore
import com.revolgenx.anilib.common.data.store.GeneralPreferencesDataStore
import com.revolgenx.anilib.common.data.store.MediaListFilterDataStore
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.list.data.field.MediaListCollectionField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.type.MediaType

class MangaListViewModel(
    mediaListService: MediaListService,
    mediaListEntryService: MediaListEntryService,
    authPreferencesDataStore: AuthPreferencesDataStore,
    mediaListFilterDataStore: MediaListFilterDataStore,
    generalPreferencesDataStore: GeneralPreferencesDataStore
) : MediaListViewModel(
    mediaListService,
    mediaListEntryService,
    authPreferencesDataStore,
    mediaListFilterDataStore,
    generalPreferencesDataStore
) {
    override val field: MediaListCollectionField = MediaListCollectionField(MediaType.MANGA)
}