package com.revolgenx.anilib.media.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import kotlinx.coroutines.flow.single

class MediaStaffPagingSource(
    field: MediaStaffField,
    private val service: MediaService
) : BasePagingSource<StaffEdgeModel, MediaStaffField>(field) {

    override suspend fun loadPage(): PageModel<StaffEdgeModel> {
        return service.getMediaStaffList(field).single()
    }

}