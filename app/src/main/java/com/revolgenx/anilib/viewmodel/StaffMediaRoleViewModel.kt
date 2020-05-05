package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.service.staff.StaffService
import com.revolgenx.anilib.source.StaffMediaRoleSource

class StaffMediaRoleViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaRoleSource, StaffMediaRoleField>() {
    override fun createSource(field: StaffMediaRoleField): StaffMediaRoleSource {
        return StaffMediaRoleSource(field, staffService, compositeDisposable)
    }
}
