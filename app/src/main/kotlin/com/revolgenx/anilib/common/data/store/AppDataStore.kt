package com.revolgenx.anilib.common.data.store

import androidx.datastore.preferences.core.intPreferencesKey
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel


class AppDataStore(override val dataStore: PreferencesDataStore): BasePreferenceDataStore() {
    companion object{
        val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")
    }

    val mediaCoverImageType = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = mediaCoverImageTypeKey,
        defaultValue = MediaCoverImageModel.type_large
    )

}

