package com.revolgenx.anilib.staff.data.service

import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.ui.model.StaffModel
import kotlinx.coroutines.flow.Flow

interface StaffService {
    fun getStaff(field: StaffField): Flow<StaffModel?>
}