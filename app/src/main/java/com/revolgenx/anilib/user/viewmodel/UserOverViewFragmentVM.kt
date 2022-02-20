package com.revolgenx.anilib.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.user.data.field.UserOverViewField
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.service.UserService

class UserOverViewFragmentVM(
    private val userService: UserService,
    private val userContainerSharedVM: UserContainerSharedVM
) : BaseViewModel() {
    val field = UserOverViewField()
    val overviewLiveData = MutableLiveData<Resource<UserModel>>()

    fun getUserOverView() {
        overviewLiveData.value = Resource.loading()
        userService.getUserOverView(field, compositeDisposable) {
            overviewLiveData.value = it
        }
    }
}