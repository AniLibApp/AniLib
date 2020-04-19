package com.revolgenx.anilib.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.user.UserField
import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.model.user.UserFollowersModel
import com.revolgenx.anilib.model.user.UserFollowerCountModel
import com.revolgenx.anilib.model.user.UserProfileModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface UserService {
    val userProfileLiveData: MutableLiveData<Resource<UserProfileModel>>
    val userFollowerCountLiveData: MutableLiveData<Resource<UserFollowerCountModel>>

    fun getUserProfile(userField: UserField, compositeDisposable: CompositeDisposable)
    fun getTotalFollower(
        userField: UserField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)? = null
    )

    fun getTotalFollowing(
        userField: UserField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<Int>) -> Unit)? = null
    )

    fun getFollowersUsers(
        userField: UserFollowerField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<UserFollowersModel>>) -> Unit
    )

}