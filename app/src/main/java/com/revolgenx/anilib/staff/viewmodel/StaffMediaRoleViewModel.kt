package com.revolgenx.anilib.staff.viewmodel

import com.revolgenx.anilib.staff.service.StaffService
import com.revolgenx.anilib.infrastructure.source.StaffMediaRoleSource
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class StaffMediaRoleViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaRoleSource, StaffMediaRoleField>() {
    override var field: StaffMediaRoleField = StaffMediaRoleField()

    override fun createSource(): StaffMediaRoleSource {
        return StaffMediaRoleSource(field, staffService, compositeDisposable)
    }
}