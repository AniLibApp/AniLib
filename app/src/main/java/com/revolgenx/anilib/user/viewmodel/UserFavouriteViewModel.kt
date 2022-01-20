package com.revolgenx.anilib.user.viewmodel

import com.revolgenx.anilib.user.data.field.UserFavouriteField
import com.revolgenx.anilib.user.service.UserService
import com.revolgenx.anilib.infrastructure.source.UserFavouriteSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel


class UserFavouriteViewModel(private val userService: UserService) :
    SourceViewModel<UserFavouriteSource, UserFavouriteField>() {

    override var field: UserFavouriteField = UserFavouriteField()

    override fun createSource(): UserFavouriteSource {
        source = UserFavouriteSource(field, userService, compositeDisposable)
        return source!!
    }
}
