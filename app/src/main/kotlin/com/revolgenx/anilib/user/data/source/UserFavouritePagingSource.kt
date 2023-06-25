package com.revolgenx.anilib.user.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.user.data.field.UserFavouriteField
import com.revolgenx.anilib.user.data.service.UserService
import kotlinx.coroutines.flow.single

class UserFavouritePagingSource(field: UserFavouriteField, private val service: UserService) :
    BasePagingSource<BaseModel, UserFavouriteField>(field) {
    override suspend fun loadPage(): PageModel<BaseModel> {
        return service.getUserFavourite(field).single()
    }
}