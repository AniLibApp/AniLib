package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ToggleLikeV2Mutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.LikeableType

data class ToggleLikeV2Field(
    var id: Int,
    var type: LikeableType
) : BaseField<ToggleLikeV2Mutation>() {
    override fun toQueryOrMutation(): ToggleLikeV2Mutation {
        return ToggleLikeV2Mutation(
            id = nn(id),
            type = nn(type)
        )
    }
}