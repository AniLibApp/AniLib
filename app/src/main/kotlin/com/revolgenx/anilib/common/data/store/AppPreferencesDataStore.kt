package com.revolgenx.anilib.common.data.store

import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.common.data.model.PreferenceDataStoreModel
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel


class AppPreferencesDataStore(override val dataStore: PreferencesDataStore):
    IPreferencesDataStore {
    companion object{
        val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")
    }

    val mediaCoverImageType = PreferenceDataStoreModel(
        dataStore = dataStore,
        prefKey = mediaCoverImageTypeKey,
        defaultValue = MediaCoverImageModel.type_large
    )

}

