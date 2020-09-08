package com.revolgenx.anilib.viewmodel.user

import com.revolgenx.anilib.field.user.UserFavouriteField
import com.revolgenx.anilib.service.user.UserService
import com.revolgenx.anilib.source.UserFavouriteSource
import com.revolgenx.anilib.viewmodel.SourceViewModel


class UserFavouriteViewModel(private val userService: UserService) :
    SourceViewModel<UserFavouriteSource, UserFavouriteField>() {

    override var field: UserFavouriteField = UserFavouriteField()

    override fun createSource(): UserFavouriteSource {
        source = UserFavouriteSource(field, userService, compositeDisposable)
        return source!!
    }
}
