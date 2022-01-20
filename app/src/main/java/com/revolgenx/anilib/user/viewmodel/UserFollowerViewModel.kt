package com.revolgenx.anilib.user.viewmodel

import com.revolgenx.anilib.user.data.field.UserFollowerField
import com.revolgenx.anilib.user.service.UserService
import com.revolgenx.anilib.infrastructure.source.UserFollowersSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class UserFollowerViewModel(private val userService: UserService) :
    SourceViewModel<UserFollowersSource, UserFollowerField>() {

    override var field = UserFollowerField()

    override fun createSource(): UserFollowersSource {
        source = UserFollowersSource(field, userService, compositeDisposable)
        return source!!
    }


}
