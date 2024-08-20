package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.constant.ExploreSectionOrder
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch

data class ExploreSectionState(val exploreSectionOrder: ExploreSectionOrder, val order: Int, var isEnabled: Boolean)

class GeneralSettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore
) : ViewModel() {
    var sectionOrder = ExploreSectionOrder.entries.map {
        ExploreSectionState(
            exploreSectionOrder = it,
            order = appPreferencesDataStore.getExploreSectionOrder(it),
            isEnabled = appPreferencesDataStore.isExploreSectionEnabled(it)
        )
    }.sortedBy { it.order }

    var exploreSectionOrderState by mutableStateOf(sectionOrder)

    fun updateExploreSectionSettings(){
        launch {
            sectionOrder = exploreSectionOrderState.mapIndexed { index, exploreSectionState ->
                appPreferencesDataStore.setExploreSectionOrder(exploreSectionState.exploreSectionOrder, index)
                appPreferencesDataStore.setExploreSectionEnabled(exploreSectionState.exploreSectionOrder, exploreSectionState.isEnabled)
                exploreSectionState.copy(order = index)
            }
        }
    }
}
