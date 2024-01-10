package com.revolgenx.anilib.media.ui.viewmodel

import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.data.store.MediaFilterData

abstract class MediaFilterBottomSheetViewModel(
    private val mediaFilterDataDataStore: DataStore<MediaFilterData>
) : BaseViewModel<MediaField>() {
    override var field: MediaField = mediaFilterDataDataStore.data.get().toMediaField()

    init {
        launch {
            mediaFilterDataDataStore.data.collect {
                field = it.toMediaField()
            }
        }
    }

    fun updateFilter() {
        launch {
            mediaFilterDataDataStore.updateData { field.toMediaFilter() }
        }
    }
}