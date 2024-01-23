package com.revolgenx.anilib.social.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.ActivityUnionFilterDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.social.data.field.ActivityUnionField

class ActivityUnionFilterViewModel(
    private val activityUnionFilterDataStore: ActivityUnionFilterDataStore
) : ViewModel() {
    var field = ActivityUnionField()

    fun updateFilter() {
        launch {
            activityUnionFilterDataStore.updateData {
                field.toFilterData()
            }
        }
    }
}