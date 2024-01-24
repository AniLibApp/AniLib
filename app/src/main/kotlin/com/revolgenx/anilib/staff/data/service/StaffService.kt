package com.revolgenx.anilib.staff.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.ui.model.StaffModel
import kotlinx.coroutines.flow.Flow

interface StaffService {
    fun getStaff(field: StaffField): Flow<StaffModel?>
    fun getStaffMediaCharacter(field: StaffMediaCharacterField): Flow<PageModel<MediaModel>>
    fun getStaffMediaRole(field: StaffMediaRoleField): Flow<PageModel<MediaModel>>
    fun toggleFavorite(staffId: Int): Flow<Boolean>
}