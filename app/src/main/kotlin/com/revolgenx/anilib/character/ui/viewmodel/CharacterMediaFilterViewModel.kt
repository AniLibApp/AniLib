package com.revolgenx.anilib.character.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.character.data.field.CharacterMediaField
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore

class CharacterMediaFilterViewModel(appPreferencesDataStore: AppPreferencesDataStore): ViewModel() {
    val titleType = appPreferencesDataStore.mediaTitleType.get()
    var field = CharacterMediaField()
}