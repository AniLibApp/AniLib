package com.revolgenx.anilib.studio.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.service.StudioService
import com.revolgenx.anilib.studio.data.source.StudioPagingSource
import com.revolgenx.anilib.studio.ui.model.StudioModel
import kotlinx.coroutines.flow.single

class StudioViewModel(private val studioService: StudioService) :
    PagingViewModel<BaseModel, StudioField, StudioPagingSource>() {
    override var field: StudioField = StudioField()
    override val pagingSource: StudioPagingSource
        get() = StudioPagingSource(this.field, studioService)

    val studio = mutableStateOf<StudioModel?>(null)
    val showToggleErrorMsg = mutableStateOf(false)

    fun toggleFavorite(){
        if (field.studioId == -1) return
        val studioModel = studio.value ?:return

        val isFavourite = studioModel.isFavourite
        isFavourite.value = !isFavourite.value
        launch {
            val toggled = studioService.toggleFavorite(field.studioId).single()
            if(!toggled){
                isFavourite.value = !isFavourite.value
                showToggleErrorMsg.value = true
            }
        }
    }
}