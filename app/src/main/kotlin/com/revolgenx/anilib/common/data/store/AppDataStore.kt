package com.revolgenx.anilib.common.data.store

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType
import kotlinx.coroutines.flow.map


val mediaTitleTypeKey = intPreferencesKey("media_title_type_key")
val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")

fun AppDataStore.mediaTitleType() =
    data.map { MediaTitleType.values()[it[mediaTitleTypeKey] ?: 0] }

fun AppDataStore.mediaCoverImageType() =
    data.map { MediaCoverImageType.values()[it[mediaCoverImageTypeKey] ?: 1] }

suspend fun AppDataStore.mediaTitleType(mediaTitleType: MediaTitleType) = edit {
    it[mediaTitleTypeKey] = mediaTitleType.ordinal
}
suspend fun AppDataStore.mediaCoverImageType(mediaCoverImageType: MediaCoverImageType) = edit {
    it[mediaCoverImageTypeKey] = mediaCoverImageType.ordinal
}