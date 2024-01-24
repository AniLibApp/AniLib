package com.revolgenx.anilib.staff.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField

class StaffMediaCharacterFilterViewModel(val mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore): ViewModel() {
    val titleType = mediaSettingsPreferencesDataStore.mediaTitleType.get()
    var field = StaffMediaCharacterField()
}