package com.revolgenx.anilib.ui.viewmodel.user

import com.revolgenx.anilib.data.field.user.UserFavouriteField
import com.revolgenx.anilib.infrastructure.service.user.UserService
import com.revolgenx.anilib.infrastructure.source.UserFavouriteSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel


class UserFavouriteViewModel(private val userService: UserService) :
    SourceViewModel<UserFavouriteSource, UserFavouriteField>() {

    override var field: UserFavouriteField = UserFavouriteField()

    override fun createSource(): UserFavouriteSource {
        source = UserFavouriteSource(field, userService, compositeDisposable)
        return source!!
    }
}
