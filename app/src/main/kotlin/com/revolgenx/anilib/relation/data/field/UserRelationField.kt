package com.revolgenx.anilib.relation.data.field

import com.revolgenx.anilib.UserRelationQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField

data class UserRelationField(val isFollower: Boolean) : BaseSourceUserField<UserRelationQuery>() {
    override fun toQueryOrMutation(): UserRelationQuery {
        return UserRelationQuery(
            page = page,
            perPage = perPage,
            isFollower = isFollower,
            userId = userId ?: -1
        )
    }
}