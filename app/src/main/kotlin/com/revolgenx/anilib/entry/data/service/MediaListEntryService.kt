package com.revolgenx.anilib.entry.data.service

import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import kotlinx.coroutines.flow.Flow

interface MediaListEntryService {
    fun saveMediaListEntry(field: SaveMediaListEntryField): Flow<MediaListModel?>
    fun getMediaListEntry(field: MediaListEntryField): Flow<UserMediaModel>
    fun deleteMediaListEntry(id: Int): Flow<Boolean>
    fun increaseProgress(mediaList: MediaListModel): Flow<MediaListModel?>
}