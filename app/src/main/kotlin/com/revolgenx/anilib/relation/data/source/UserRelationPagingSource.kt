package com.revolgenx.anilib.relation.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.relation.data.field.UserRelationField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.single

class UserRelationPagingSource(
    field: UserRelationField,
    private val userService: UserService
) : BasePagingSource<UserModel, UserRelationField>(field) {
    override suspend fun loadPage(): PageModel<UserModel> {
        return userService.getUserRelation(field).single()
    }
}