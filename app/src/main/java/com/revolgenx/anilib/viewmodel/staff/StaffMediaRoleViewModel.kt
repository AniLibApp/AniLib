package com.revolgenx.anilib.viewmodel.staff

import com.revolgenx.anilib.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.service.staff.StaffService
import com.revolgenx.anilib.source.StaffMediaRoleSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class StaffMediaRoleViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaRoleSource, StaffMediaRoleField>() {
    override var field: StaffMediaRoleField = StaffMediaRoleField()

    override fun createSource(): StaffMediaRoleSource {
        return StaffMediaRoleSource(field, staffService, compositeDisposable)
    }
}
