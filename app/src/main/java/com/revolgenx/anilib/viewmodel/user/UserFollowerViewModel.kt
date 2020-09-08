package com.revolgenx.anilib.viewmodel.user

import com.revolgenx.anilib.field.user.UserFollowerField
import com.revolgenx.anilib.service.user.UserService
import com.revolgenx.anilib.source.UserFollowersSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class UserFollowerViewModel(private val userService: UserService) :
    SourceViewModel<UserFollowersSource, UserFollowerField>() {

    override var field = UserFollowerField()

    override fun createSource(): UserFollowersSource {
        source = UserFollowersSource(field, userService, compositeDisposable)
        return source!!
    }


}
