package com.revolgenx.anilib.ui.viewmodel.friend

import com.revolgenx.anilib.data.field.friend.UserFriendField
import com.revolgenx.anilib.infrastructure.service.user.UserService
import com.revolgenx.anilib.infrastructure.source.friend.UserFriendSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class UserFriendFragmentViewModel(private val userService: UserService) : SourceViewModel<UserFriendSource, UserFriendField>() {
    override var field: UserFriendField = UserFriendField()

    override fun createSource(): UserFriendSource {
        source = UserFriendSource(field, userService, compositeDisposable)
        return source!!
    }

}
