package com.revolgenx.anilib.ui.viewmodel.staff

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.field.staff.StaffField
import com.revolgenx.anilib.data.model.StaffModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.ToggleService
import com.revolgenx.anilib.infrastructure.service.staff.StaffService
import io.reactivex.disposables.CompositeDisposable

class StaffViewModel(
    private val staffService: StaffService, private val toggleService: ToggleService
) : ViewModel() {
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    val staffInfoLiveData by lazy {
        MediatorLiveData<Resource<StaffModel>>().also {
            it.addSource(staffService.staffInfoLiveData) { res ->
                it.value = res
            }
        }
    }

    val toggleStaffFavLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().also {
            it.addSource(toggleService.toggleFavMutableLiveData) { res ->
                it.value = res
            }
        }
    }

    fun getStaffInfo(field: StaffField) {
        staffInfoLiveData.value = Resource.loading(null)
        staffService.getStaffInfo(field, compositeDisposable)
    }

    fun toggleCharacterFav(field: ToggleFavouriteField) {
        toggleStaffFavLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(field, compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
