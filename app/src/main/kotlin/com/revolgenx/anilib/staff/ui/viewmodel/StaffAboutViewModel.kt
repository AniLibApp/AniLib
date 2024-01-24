package com.revolgenx.anilib.staff.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.service.StaffService
import com.revolgenx.anilib.staff.ui.model.StaffModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class StaffAboutViewModel(private val staffService: StaffService) :
    ResourceViewModel<StaffModel, StaffField>() {
    override val field: StaffField = StaffField()

    val showToggleErrorMsg = mutableStateOf(false)

    override fun load(): Flow<StaffModel?> {
        return staffService.getStaff(field)
    }

    fun toggleFavorite() {
        if (field.staffId == -1) return
        val isFavourite = getData()?.isFavourite ?: return
        isFavourite.value = !isFavourite.value

        launch {
            val toggled = staffService.toggleFavorite(field.staffId).single()
            if (!toggled) {
                isFavourite.value = !isFavourite.value
                showToggleErrorMsg.value = true
            }
        }
    }
}