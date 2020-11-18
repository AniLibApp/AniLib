package com.revolgenx.anilib.ui.viewmodel.user

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.user.UserProfileField
import com.revolgenx.anilib.data.field.user.UserToggleFollowField
import com.revolgenx.anilib.data.model.user.UserFollowerCountModel
import com.revolgenx.anilib.data.model.user.UserProfileModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.user.UserService
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

    val toggleFollowField: UserToggleFollowField by lazy {
        UserToggleFollowField()
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

    fun toggleFollow(callback: (Resource<Boolean>) -> Unit) {
        callback.invoke(Resource.loading(null))
        userService.toggleUserFollowing(toggleFollowField, compositeDisposable) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.isFollowing?.let {
                        userProfileLiveData.value?.data?.isFollowing = it
                        callback.invoke(Resource.success(it))
                    }
                }
                Status.ERROR -> {
                    callback.invoke(Resource.error(it.message!!, null, it.exception))
                }
                else -> {}
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}
