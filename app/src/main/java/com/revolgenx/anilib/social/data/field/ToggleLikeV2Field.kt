package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ToggleLikeV2Mutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.LikeableType

class ToggleLikeV2Field : BaseField<ToggleLikeV2Mutation>() {
    var id: Int? = null
    var type: LikeableType? = null
    override fun toQueryOrMutation(): ToggleLikeV2Mutation {
        return ToggleLikeV2Mutation(
            id = nn(id),
            type = nn(type)
        )
    }
}