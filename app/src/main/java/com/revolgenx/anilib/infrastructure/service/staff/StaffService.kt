package com.revolgenx.anilib.infrastructure.service.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.data.field.staff.StaffField
import com.revolgenx.anilib.data.field.staff.StaffMediaCharacterField
import com.revolgenx.anilib.data.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.data.model.StaffMediaCharacterModel
import com.revolgenx.anilib.data.model.StaffMediaRoleModel
import com.revolgenx.anilib.data.model.StaffModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface StaffService {
    val staffInfoLiveData: MutableLiveData<Resource<StaffModel>>

    fun getStaffInfo(
        field: StaffField,
        compositeDisposable: CompositeDisposable
    ): LiveData<Resource<StaffModel>>

    fun getStaffMediaCharacter(
        field: StaffMediaCharacterField, compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<StaffMediaCharacterModel>>) -> Unit)
    )

    fun getStaffMediaRole(
        field: StaffMediaRoleField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<StaffMediaRoleModel>>) -> Unit)
    )
}