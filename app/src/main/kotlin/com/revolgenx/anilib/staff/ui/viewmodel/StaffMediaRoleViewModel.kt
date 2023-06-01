package com.revolgenx.anilib.staff.ui.viewmodel

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.data.source.StaffMediaRolePagingSource

class StaffMediaRoleViewModel(
    private val service: StaffService
) : PagingViewModel<BaseModel, StaffMediaRoleField, StaffMediaRolePagingSource>() {
    override val field: StaffMediaRoleField = StaffMediaRoleField()
    override val pagingSource: StaffMediaRolePagingSource
        get() = StaffMediaRolePagingSource(this.field, service)
}