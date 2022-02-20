package com.revolgenx.anilib.user.service

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.friend.data.field.UserFriendField
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.user.data.field.*
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.disposables.CompositeDisposable

interface UserService {
    fun getUserOverView(
        field: UserOverViewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<UserModel>) -> Unit
    )

    fun getUserFollowingFollowerCount(
        field: UserFollowingFollowerCountField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Pair<Int, Int>>) -> Unit
    )

    fun getFollowersUsers(
        field: UserFollowerField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<UserModel>>) -> Unit
    )


    fun getUserFavourite(
        field: UserFavouriteField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseModel>>) -> Unit
    )

    fun getUserFriend(
        field: UserFriendField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<UserModel>>) -> Unit)
    )

}