package com.revolgenx.anilib.staff.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.ui.model.StaffModel
import kotlinx.coroutines.flow.Flow

class StaffAboutViewModel(private val staffService: StaffService) :
    ResourceViewModel<StaffModel, StaffField>() {
    override val field: StaffField = StaffField()

    override fun loadData(): Flow<StaffModel?> {
        return staffService.getStaff(field)
    }
}