package com.revolgenx.anilib.user.ui.viewmodel

import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.user.data.field.UserFavouriteField
import com.revolgenx.anilib.user.data.field.UserFavouriteType
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.data.source.UserFavouritePagingSource

class UserFavouriteContentViewModel(
    favouriteType: UserFavouriteType,
    private val service: UserService
) : PagingViewModel<BaseModel, UserFavouriteField, UserFavouritePagingSource>() {
    override val field: UserFavouriteField = UserFavouriteField(favouriteType)
    override val pagingSource: UserFavouritePagingSource
        get() = UserFavouritePagingSource(this.field, service)
}