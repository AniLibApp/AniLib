package com.revolgenx.anilib.studio.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import com.revolgenx.anilib.studio.data.field.StudioField

class StudioFilterViewModel(mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore):ViewModel() {
    val titleType = mediaSettingsPreferencesDataStore.mediaTitleType.get()
    var field = StudioField()
}