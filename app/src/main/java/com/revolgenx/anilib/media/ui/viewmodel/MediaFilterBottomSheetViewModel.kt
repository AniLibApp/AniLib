package com.revolgenx.anilib.media.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.store.MediaFieldData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MediaFilterBottomSheetViewModel(
    private val seasonFilterStore: DataStore<MediaFieldData>
) : ViewModel() {
    var field by mutableStateOf(MediaField())

    init {
        viewModelScope.launch {
            field = seasonFilterStore.data.first().toFieldIfDifferent(field)
            seasonFilterStore.data.collect {
                field = it.toFieldIfDifferent(field)
            }
        }
    }

    fun updateFilter() {
        viewModelScope.launch {
            seasonFilterStore.updateData {
                field.toData()
            }
        }
    }
}