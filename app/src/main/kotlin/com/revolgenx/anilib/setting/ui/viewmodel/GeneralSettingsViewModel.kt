package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.constant.ContentOrder
import com.revolgenx.anilib.common.data.constant.ExploreSectionOrder
import com.revolgenx.anilib.common.data.constant.MainPageOrder
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch

data class ContentOrderData<T: ContentOrder>(val value: T, val order: Int, var isEnabled: Boolean)

class GeneralSettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore
) : ViewModel() {
    var exploreSectionOrder = ExploreSectionOrder.entries.map {
        ContentOrderData(
            value = it,
            order = appPreferencesDataStore.getExploreSectionOrder(it),
            isEnabled = appPreferencesDataStore.isExploreSectionEnabled(it)
        )
    }.sortedBy { it.order }



    var mainPageOrder = MainPageOrder.entries.map {
        ContentOrderData(
            value = it,
            order = appPreferencesDataStore.getMainPageOrder(it),
            isEnabled = true
        )
    }.sortedBy { it.order }

    var exploreSectionOrderState by mutableStateOf(exploreSectionOrder)
    var mainPageOrderState by mutableStateOf(mainPageOrder)

    fun updateExploreSectionSettings(){
        launch {
            exploreSectionOrder = exploreSectionOrderState.mapIndexed { index, exploreSectionState ->
                appPreferencesDataStore.setExploreSectionOrder(exploreSectionState.value, index)
                appPreferencesDataStore.setExploreSectionEnabled(exploreSectionState.value, exploreSectionState.isEnabled)
                exploreSectionState.copy(order = index)
            }
        }
    }

    fun updateMainPageSettings(){
        launch {
            mainPageOrder = mainPageOrderState.mapIndexed { index, mainPageState ->
                appPreferencesDataStore.setMainPageOrder(mainPageState.value, index)
                mainPageState.copy(order = index)
            }
        }
    }
}
