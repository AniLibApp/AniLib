package com.revolgenx.anilib.relation.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.relation.data.field.UserRelationField
import com.revolgenx.anilib.relation.data.source.UserRelationPagingSource
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.UserModel


enum class UserRelationType {
    USER_RELATION_FOLLOWING,
    USER_RELATION_FOLLOWER
}

class UserRelationViewModel(isFollower: Boolean, private val userService: UserService) :
    PagingViewModel<UserModel, UserRelationField, UserRelationPagingSource>() {

    override val field: UserRelationField = UserRelationField(isFollower)
    override val pagingSource: UserRelationPagingSource
        get() = UserRelationPagingSource(this.field, userService)
}