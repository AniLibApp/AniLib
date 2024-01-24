package com.revolgenx.anilib.character.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.setting.data.store.MediaSettingsPreferencesDataStore

class CharacterMediaFilterViewModel(mediaSettingsPreferencesDataStore: MediaSettingsPreferencesDataStore): ViewModel() {
    val titleType = mediaSettingsPreferencesDataStore.mediaTitleType.get()
    var field = CharacterMediaField()
}