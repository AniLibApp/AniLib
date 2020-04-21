package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.user.UserFavouriteField
import com.revolgenx.anilib.service.user.UserService
import com.revolgenx.anilib.source.UserFavouriteSource


class UserFavouriteViewModel(private val userService: UserService) :
    SourceViewModel<UserFavouriteSource, UserFavouriteField>() {

    val field = UserFavouriteField()

    override fun createSource(field: UserFavouriteField): UserFavouriteSource {
        source = UserFavouriteSource(field, userService, compositeDisposable)
        return source!!
    }
}
