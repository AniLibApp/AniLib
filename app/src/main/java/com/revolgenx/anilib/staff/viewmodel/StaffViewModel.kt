package com.revolgenx.anilib.staff.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.staff.service.StaffService
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService

class StaffViewModel(
    private val staffService: StaffService
    , private val toggleService: ToggleService
) : BaseViewModel() {

    val staffInfoLiveData = MutableLiveData<Resource<StaffModel>>()
    val toggleStaffFavLiveData = MutableLiveData<Resource<Boolean>>()

    val staffField = StaffField()
    val staffToggleField = ToggleFavouriteField()

    fun getStaffInfo(field: StaffField) {
        staffInfoLiveData.value = Resource.loading(null)
        staffService.getStaffInfo(field, compositeDisposable) {
            staffInfoLiveData.value = it
        }
    }

    fun toggleCharacterFav(field: ToggleFavouriteField) {
        toggleStaffFavLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(field, compositeDisposable) {
            toggleStaffFavLiveData.value = it
        }
    }


}