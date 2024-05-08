package com.revolgenx.anilib.list.data.store

import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


enum class MediaListEntryEventType{
    CREATED, UPDATED, DELETED
}
class MediaListEntryEventStore {
    private val _mediaListUpdate = MutableSharedFlow<Pair<MediaListEntryEventType, MediaListModel>>()
    val mediaListUpdate:SharedFlow<Pair<MediaListEntryEventType, MediaListModel>> = _mediaListUpdate

    suspend fun create(model: MediaListModel){
        _mediaListUpdate.emit(MediaListEntryEventType.CREATED to model)
    }

    suspend fun update(model: MediaListModel){
        _mediaListUpdate.emit(MediaListEntryEventType.UPDATED to model)
    }

    suspend fun delete(model: MediaListModel){
        _mediaListUpdate.emit(MediaListEntryEventType.DELETED to model)
    }
}