package com.revolgenx.anilib.home.explore.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.constant.ExploreSectionOrder
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.setting.ui.viewmodel.ExploreSectionState

class ExploreViewModel(appPreferencesDataStore: AppPreferencesDataStore): ViewModel(), Runnable {
    private val handler = Handler(Looper.getMainLooper())
    var showRefreshButton by mutableStateOf(false)

    val exploreSectionOrderState = ExploreSectionOrder.entries.map {
        ExploreSectionState(
            exploreSectionOrder = it,
            order = appPreferencesDataStore.getExploreSectionOrder(it),
            isEnabled = appPreferencesDataStore.isExploreSectionEnabled(it)
        )
    }.sortedBy { it.order }

    override fun run() {
        showRefreshButton = true
        handler.postDelayed(this, 600000)
    }

    init {
        handler.postDelayed(this, 600000)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}