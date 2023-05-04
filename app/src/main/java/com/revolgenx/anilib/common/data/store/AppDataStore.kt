package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType
import kotlinx.coroutines.flow.map


private val mediaTitleTypeKey = intPreferencesKey("media_title_type_key")
private val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")

fun AppPreferencesDataStore.mediaTitleType() =
    data.map { MediaTitleType.values()[it[mediaTitleTypeKey] ?: 0] }

fun AppPreferencesDataStore.mediaCoverImageType() =
    data.map { MediaCoverImageType.values()[it[mediaCoverImageTypeKey] ?: 1] }

suspend fun AppPreferencesDataStore.mediaTitleType(mediaTitleType: MediaTitleType) = edit {
    it[mediaTitleTypeKey] = mediaTitleType.ordinal
}
suspend fun AppPreferencesDataStore.mediaCoverImageType(mediaCoverImageType: MediaCoverImageType) = edit {
    it[mediaCoverImageTypeKey] = mediaCoverImageType.ordinal
}

fun Context.mediaTitleType() = this.appPreferencesDataStore.mediaTitleType()
fun Context.mediaCoverImageType() = this.appPreferencesDataStore.mediaCoverImageType()