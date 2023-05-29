package com.revolgenx.anilib.staff.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.data.source.StaffMediaCharacterSource

class StaffMediaCharacterViewModel(private val service: StaffService) :
    PagingViewModel<MediaModel, StaffMediaCharacterField, StaffMediaCharacterSource>() {
    override val field: StaffMediaCharacterField = StaffMediaCharacterField()
    override val pagingSource: StaffMediaCharacterSource
        get() = StaffMediaCharacterSource(this.field, service)
}