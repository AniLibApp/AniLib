package com.revolgenx.anilib.studio.ui.viewmodel

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.service.StudioService
import com.revolgenx.anilib.studio.data.source.StudioPagingSource

class StudioViewModel(private val studioService: StudioService) :
    PagingViewModel<BaseModel, StudioField, StudioPagingSource>() {
    override val field: StudioField = StudioField()
    override val pagingSource: StudioPagingSource
        get() = StudioPagingSource(this.field, studioService)
}