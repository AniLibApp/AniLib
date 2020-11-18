package com.revolgenx.anilib.ui.viewmodel.user

import com.revolgenx.anilib.data.field.user.UserFollowerField
import com.revolgenx.anilib.infrastructure.service.user.UserService
import com.revolgenx.anilib.infrastructure.source.UserFollowersSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class UserFollowerViewModel(private val userService: UserService) :
    SourceViewModel<UserFollowersSource, UserFollowerField>() {

    override var field = UserFollowerField()

    override fun createSource(): UserFollowersSource {
        source = UserFollowersSource(field, userService, compositeDisposable)
        return source!!
    }


}
