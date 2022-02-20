package com.revolgenx.anilib.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.user.data.field.UserToggleFollowField
import com.revolgenx.anilib.user.data.model.UserModel

class UserContainerSharedVM(private val toggleService: ToggleService) : BaseViewModel() {
    var userId: Int? = null
        set(value) {
            field = value
            toggleFollowField.userId = value
        }
    var userName: String? = null
        set(value) {
            field = value
            toggleFollowField.userName = value
        }

    val hasUserData get()= takeIf { userId != null || userName != null }
    private val toggleFollowField = UserToggleFollowField()

    val userLiveData = MutableLiveData<Resource<UserModel>>()
    val toggleFollowLiveData = MutableLiveData<Resource<UserModel>>()

    fun toggleFollow() {
        toggleService.toggleUserFollow(toggleFollowField, compositeDisposable) {
            userLiveData.value?.data?.isFollowing = it.data?.isFollowing == true
            toggleFollowLiveData.value = it
        }
    }
}