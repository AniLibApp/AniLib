package com.revolgenx.anilib.list.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter

sealed class MediaListFilterViewModel(appPreferencesDataStore: AppPreferencesDataStore) : ViewModel() {
    lateinit var filter: MediaListCollectionFilter
    val canShowAdultContent = appPreferencesDataStore.displayAdultContent
}

class AnimeListFilterViewModel(appPreferencesDataStore: AppPreferencesDataStore) : MediaListFilterViewModel(appPreferencesDataStore)
class MangaListFilterViewModel(appPreferencesDataStore: AppPreferencesDataStore) : MediaListFilterViewModel(appPreferencesDataStore)