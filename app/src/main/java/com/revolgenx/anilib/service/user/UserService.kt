package com.revolgenx.anilib.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.user.UserFavouriteField
import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.field.user.UserProfileField
import com.revolgenx.anilib.field.user.UserToggleFollowField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.user.UserFollowerCountModel
import com.revolgenx.anilib.model.user.UserFollowersModel
import com.revolgenx.anilib.model.user.UserProfileModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface UserService {
    val userProfileLiveData: MutableLiveData<Resource<UserProfileModel>>
    val userFollowerCountLiveData: MutableLiveData<Resource<UserFollowerCountModel>>

    fun getUserProfile(userProfileField: UserProfileField, compositeDisposable: CompositeDisposable)
    fun getTotalFollower(
        userProfileField: UserProfileField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)? = null
    )

    fun getTotalFollowing(
        userProfileField: UserProfileField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)? = null
    )

    fun getFollowersUsers(
        userField: UserFollowerField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<UserFollowersModel>>) -> Unit
    )


    fun getUserFavourite(
        field: UserFavouriteField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseModel>>) -> Unit
    )

    fun toggleUserFollowing(
        field: UserToggleFollowField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<UserProfileModel>) -> Unit)?
    )

}