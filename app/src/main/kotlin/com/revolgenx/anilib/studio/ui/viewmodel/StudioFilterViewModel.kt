package com.revolgenx.anilib.studio.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.studio.data.field.StudioField

class StudioFilterViewModel(appPreferencesDataStore: AppPreferencesDataStore):ViewModel() {
    val titleType = appPreferencesDataStore.mediaTitleType.get()
    var field = StudioField()
}