package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.service.user.UserService
import com.revolgenx.anilib.source.UserFollowersSource

class UserFollowerViewModel(private val userService: UserService) :
    SourceViewModel<UserFollowersSource, UserFollowerField>() {

    val field = UserFollowerField()

    override fun createSource(field: UserFollowerField): UserFollowersSource {
        source = UserFollowersSource(field, userService, compositeDisposable)
        return source!!
    }


}
