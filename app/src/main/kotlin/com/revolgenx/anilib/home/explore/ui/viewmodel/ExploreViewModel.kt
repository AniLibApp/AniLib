package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.constant.ExploreSectionOrder
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.setting.ui.viewmodel.ContentOrderData
import kotlinx.coroutines.delay

class ExploreViewModel(appPreferencesDataStore: AppPreferencesDataStore): ViewModel() {
    var showRefreshButton = mutableStateOf(false)

    val exploreSectionContentOrderData = ExploreSectionOrder.entries.map {
        ContentOrderData(
            value = it,
            order = appPreferencesDataStore.getExploreSectionOrder(it),
            isEnabled = appPreferencesDataStore.isExploreSectionEnabled(it)
        )
    }.sortedBy { it.order }

    init {
        launch {
            while (true){
                if(showRefreshButton.value){
                    delay(10000)
                }else{
                    delay(900000)
                    showRefreshButton.value = true
                }
            }
        }
    }
}