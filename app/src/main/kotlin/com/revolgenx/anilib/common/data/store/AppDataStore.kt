package com.revolgenx.anilib.common.data.store

import androidx.datastore.preferences.core.intPreferencesKey


class AppDataStore(override val dataStore: PreferencesDataStore): BasePreferenceDataStore() {
    companion object{
        val mediaCoverImageTypeKey = intPreferencesKey("media_cover_image_type_key")
        const val mediumCoverImage = 1
    }

    val mediaCoverImageType = PreferenceDataModel(
        dataStore = dataStore,
        prefKey = mediaCoverImageTypeKey,
        defaultValue = mediumCoverImage
    )

}

