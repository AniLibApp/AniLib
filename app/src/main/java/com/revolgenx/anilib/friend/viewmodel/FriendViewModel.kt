package com.revolgenx.anilib.friend.viewmodel

import com.revolgenx.anilib.friend.data.field.UserFriendField
import com.revolgenx.anilib.user.service.UserService
import com.revolgenx.anilib.infrastructure.source.friend.UserFriendSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class FriendViewModel(private val userService: UserService) : SourceViewModel<UserFriendSource, UserFriendField>() {
    override var field: UserFriendField = UserFriendField()

    override fun createSource(): UserFriendSource {
        source = UserFriendSource(field, userService, compositeDisposable)
        return source!!
    }

}