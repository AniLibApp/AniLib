package com.revolgenx.anilib.user.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.friend.data.field.UserFriendField
import com.revolgenx.anilib.friend.data.model.FriendModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.user.data.field.UserFavouriteField
import com.revolgenx.anilib.user.data.field.UserFollowerField
import com.revolgenx.anilib.user.data.field.UserProfileField
import com.revolgenx.anilib.user.data.field.UserToggleFollowField
import com.revolgenx.anilib.user.data.model.UserFollowerCountModel
import com.revolgenx.anilib.user.data.model.UserFollowersModel
import com.revolgenx.anilib.user.data.model.UserProfileModel
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

    fun getUserFriend(
        field: UserFriendField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<FriendModel>>) -> Unit)
    )

}