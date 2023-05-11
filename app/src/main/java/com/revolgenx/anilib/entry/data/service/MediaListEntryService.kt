package com.revolgenx.anilib.entry.data.service

import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import kotlinx.coroutines.flow.Flow

interface MediaListEntryService {
    fun getMediaListEntry(field: MediaListEntryField): Flow<UserMediaModel>
}