package com.revolgenx.anilib.staff.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.service.StaffService
import kotlinx.coroutines.flow.single

class StaffMediaCharacterPagingSource(
    field: StaffMediaCharacterField,
    private val staffService: StaffService
) : BasePagingSource<MediaModel, StaffMediaCharacterField>(field) {
    override suspend fun loadPage(): PageModel<MediaModel> {
        return staffService.getStaffMediaCharacter(field).single()
    }
}