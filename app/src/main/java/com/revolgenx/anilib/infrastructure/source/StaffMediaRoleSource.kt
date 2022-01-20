package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.data.model.StaffMediaRoleModel
import com.revolgenx.anilib.staff.service.StaffService
import io.reactivex.disposables.CompositeDisposable

class StaffMediaRoleSource(
    field: StaffMediaRoleField,
    private val staffService: StaffService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<StaffMediaRoleModel, StaffMediaRoleField>(field) {
    override fun areItemsTheSame(first: StaffMediaRoleModel, second: StaffMediaRoleModel): Boolean {
        return first.mediaId == second.mediaId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        staffService.getStaffMediaRole(field, compositeDisposable) { res ->
            postResult(page, res)
        }
    }
}
