package com.revolgenx.anilib.ui.viewmodel.staff

import com.revolgenx.anilib.data.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.infrastructure.service.staff.StaffService
import com.revolgenx.anilib.infrastructure.source.StaffMediaRoleSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class StaffMediaRoleViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaRoleSource, StaffMediaRoleField>() {
    override var field: StaffMediaRoleField = StaffMediaRoleField()

    override fun createSource(): StaffMediaRoleSource {
        return StaffMediaRoleSource(field, staffService, compositeDisposable)
    }
}
