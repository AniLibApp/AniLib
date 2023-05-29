package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaStaffPagingSource
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel

class MediaStaffViewModel(private val mediaService: MediaService) :
    PagingViewModel<StaffEdgeModel, MediaStaffField, MediaStaffPagingSource>() {
    override val field = MediaStaffField()

    override val pagingSource: MediaStaffPagingSource
        get() = MediaStaffPagingSource(this.field, mediaService)
}