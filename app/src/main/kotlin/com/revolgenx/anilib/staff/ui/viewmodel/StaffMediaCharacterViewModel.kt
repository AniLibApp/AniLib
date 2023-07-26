package com.revolgenx.anilib.staff.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.data.source.StaffMediaCharacterPagingSource

class StaffMediaCharacterViewModel(private val service: StaffService) :
    PagingViewModel<MediaModel, StaffMediaCharacterField, StaffMediaCharacterPagingSource>() {
    override val field: StaffMediaCharacterField = StaffMediaCharacterField()
    override val pagingSource: StaffMediaCharacterPagingSource
        get() = StaffMediaCharacterPagingSource(this.field, service)
}