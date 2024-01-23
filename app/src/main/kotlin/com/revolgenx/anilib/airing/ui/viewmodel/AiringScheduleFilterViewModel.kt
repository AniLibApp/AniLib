package com.revolgenx.anilib.airing.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.ext.launch

open class AiringScheduleFilterViewModel(
    private val airingScheduleFilterDataStore: AiringScheduleFilterDataStore
) :
    ViewModel() {
    var field = AiringScheduleField()
    fun updateFilter() {
        launch {
            airingScheduleFilterDataStore.updateData {
                field.toFilterData()
            }
        }
    }
}