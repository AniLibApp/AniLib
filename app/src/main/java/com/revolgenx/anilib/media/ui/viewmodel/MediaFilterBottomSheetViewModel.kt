package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.SeasonFilterDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.media.data.field.MediaField

class MediaFilterBottomSheetViewModel(
    private val seasonFilterStore: SeasonFilterDataStore
) : ViewModel() {
    var field by mutableStateOf(MediaField())

    init {
        launch {
            seasonFilterStore.data.collect {
                field = it.toField(field)
            }
        }
    }

    fun updateFilter() {
        launch {
            seasonFilterStore.updateData {
                field.toData()
            }
        }
    }
}