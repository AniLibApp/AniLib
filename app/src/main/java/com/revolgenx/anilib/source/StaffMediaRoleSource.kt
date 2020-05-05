package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.model.StaffMediaRoleModel
import com.revolgenx.anilib.service.staff.StaffService
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
