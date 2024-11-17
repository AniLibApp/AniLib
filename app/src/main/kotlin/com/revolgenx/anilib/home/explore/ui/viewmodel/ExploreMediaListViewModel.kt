package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.list.data.field.MediaListField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.source.MediaListPagingSource
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.type.MediaListSort
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed class ExploreMediaListViewModel(
    val type: MediaType,
    private val service: MediaListService,
    private val mediaListEntryService: MediaListEntryService,
    private val appPreferencesDataStore: AppPreferencesDataStore
) : PagingViewModel<MediaListModel, MediaListField, MediaListPagingSource>() {
    override val field: MediaListField =
        MediaListField(type = type, status = MediaListStatus.CURRENT, userId = -1, sort =
        appPreferencesDataStore.data.map { if (type == MediaType.MANGA) it[AppPreferencesDataStore.exploreReadingSortKey] else it[AppPreferencesDataStore.exploreWatchingSortKey] }
            .get()?.let { MediaListSort.entries[it] })
    override val pagingSource: MediaListPagingSource
        get() = MediaListPagingSource(this.field, service)

    val openEditorScreen = appPreferencesDataStore.openMediaListEntryEditorOnClick

    var saveResource by mutableStateOf<ResourceState<Any>?>(null)

    private fun saveFailed(it: Throwable) {
        saveResource = ResourceState.error(it)
    }

    fun updateMediaListSort(sort: MediaListSort?) {
        field.sort = sort
        launch {
            appPreferencesDataStore.dataStore.edit {
                if (sort == null) {
                    it.remove(if (type == MediaType.MANGA) AppPreferencesDataStore.exploreReadingSortKey else AppPreferencesDataStore.exploreWatchingSortKey)
                } else {
                    it[if (type == MediaType.MANGA) AppPreferencesDataStore.exploreReadingSortKey else AppPreferencesDataStore.exploreWatchingSortKey] =
                        sort.ordinal
                }
            }
        }
    }

    fun increaseProgress(mediaList: MediaListModel) {
        mediaListEntryService.increaseProgress(mediaList)
            .onEach {
                it?.let {listModel->
                    mediaList.progress = listModel.progress
                    mediaList.progressState?.value = listModel.progress
                    mediaList.status.value = listModel.status.value
                    listModel.media?.mediaListEntry?.status?.value = listModel.status.value
                }
            }.catch {
                saveFailed(it)
            }.launchIn(viewModelScope)
    }

}


class ExploreWatchingViewModel(
    service: MediaListService,
    mediaListEntryService: MediaListEntryService,
    appPreferencesDataStore: AppPreferencesDataStore
) :
    ExploreMediaListViewModel(
        MediaType.ANIME,
        service,
        mediaListEntryService,
        appPreferencesDataStore
    )

class ExploreReadingViewModel(
    service: MediaListService,
    mediaListEntryService: MediaListEntryService,
    appPreferencesDataStore: AppPreferencesDataStore
) :
    ExploreMediaListViewModel(
        MediaType.MANGA,
        service,
        mediaListEntryService,
        appPreferencesDataStore
    )


