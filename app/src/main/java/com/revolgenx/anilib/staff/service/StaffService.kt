package com.revolgenx.anilib.staff.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaConnectionModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.data.field.StaffMediaRoleField
import com.revolgenx.anilib.staff.data.model.StaffModel
import io.reactivex.disposables.CompositeDisposable

interface StaffService {
    val staffInfoLiveData: MutableLiveData<Resource<StaffModel>>

    fun getStaffInfo(
        field: StaffField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<StaffModel>)->Unit
    )

    fun getStaffMediaCharacter(
        field: StaffMediaCharacterField, compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<MediaModel>>) -> Unit)
    )

    fun getStaffMediaRole(
        field: StaffMediaRoleField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<MediaModel>>) -> Unit)
    )
}