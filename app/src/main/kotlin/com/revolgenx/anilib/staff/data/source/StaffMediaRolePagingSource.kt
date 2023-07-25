package com.revolgenx.anilib.staff.data.source

import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.common.ext.getOrEmpty
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.data.service.StaffService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class StaffMediaRolePagingSource(
    field: StaffMediaRoleField,
    private val staffService: StaffService
) : BasePagingSource<BaseModel, StaffMediaRoleField>(field) {

    private var lastHeader: Int = -1

    override suspend fun loadPage(): PageModel<BaseModel> {
        return staffService.getStaffMediaRole(field).map {
            val results = mutableListOf<BaseModel>()
            for (model in it.data.getOrEmpty()) {
                val header =
                    if (model.isAnime) R.string.anime_staff_roles else R.string.manga_staff_roles
                if (header != lastHeader) {
                    results.add(HeaderModel(titleRes = header))
                    lastHeader = header
                }
                results.add(model)
            }
            PageModel(it.pageInfo, results)
        }.single()
    }
}
