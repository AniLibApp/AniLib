package com.revolgenx.anilib.common.data.store

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class AppDataStore(private val dataStore: PreferencesDataStore) {
    companion object{
        val mediaTitleTypeKey = intPreferencesKey("media_title_type_key")
        val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")
    }

    val mediaTitleType = dataStore.data.map { MediaTitleType.values()[it[mediaTitleTypeKey] ?: 0] }
    val mediaCoverImageType =
        dataStore.data.map { MediaCoverImageType.values()[it[mediaCoverImageTypeKey] ?: 1] }

    fun mediaTitleType(scope: CoroutineScope, mediaTitleType: MediaTitleType) {
        scope.launch {
            dataStore.edit {
                it[mediaTitleTypeKey] = mediaTitleType.ordinal
            }
        }
    }

    fun mediaCoverImageType(scope: CoroutineScope, mediaCoverImageType: MediaCoverImageType) {
        scope.launch {
            dataStore.edit {
                it[mediaCoverImageTypeKey] = mediaCoverImageType.ordinal
            }
        }
    }
}

