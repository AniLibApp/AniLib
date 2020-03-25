package com.revolgenx.anilib.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.StaffField
import com.revolgenx.anilib.field.StaffMediaCharacterField
import com.revolgenx.anilib.field.StaffMediaRoleField
import com.revolgenx.anilib.model.StaffMediaCharacterModel
import com.revolgenx.anilib.model.StaffMediaRoleModel
import com.revolgenx.anilib.model.StaffModel
import com.revolgenx.anilib.repository.util.Resource
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