package com.revolgenx.anilib.staff.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField

class StaffMediaRoleFilterViewModel(val appPreferencesDataStore: AppPreferencesDataStore): ViewModel() {
    val titleType = appPreferencesDataStore.mediaTitleType.get()
    var field = StaffMediaRoleField()
}