package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.user.UserProfileField
import com.revolgenx.anilib.model.user.UserFollowerCountModel
import com.revolgenx.anilib.model.user.UserProfileModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.user.UserService
import io.reactivex.disposables.CompositeDisposable

class UserProfileViewModel(private val userService: UserService) : ViewModel() {

    val userProfileLiveData = MediatorLiveData<Resource<UserProfileModel>>().also {
        it.addSource(userService.userProfileLiveData) { res ->
            it.value = res
        }
    }

    val followerLiveData = MediatorLiveData<Resource<UserFollowerCountModel>>().also {
        it.addSource(userService.userFollowerCountLiveData) { res ->
            it.value = res
        }
    }


    private val compositeDisposable = CompositeDisposable()

    val userField by lazy {
        UserProfileField()
    }

    fun getProfile() {
        userProfileLiveData.value = Resource.loading(userProfileLiveData.value?.data)
        userService.getUserProfile(userField, compositeDisposable)
    }

    fun getFollower() {
        userService.getTotalFollower(userField, compositeDisposable)
        userService.getTotalFollowing(userField, compositeDisposable)
    }

}
