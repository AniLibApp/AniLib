package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserStatisticOverviewQuery
import com.revolgenx.anilib.common.data.field.BaseUserField
import com.revolgenx.anilib.type.MediaType

data class UserStatsOverviewField(var type: MediaType) : BaseUserField<UserStatisticOverviewQuery>() {
    override fun toQueryOrMutation(): UserStatisticOverviewQuery {
        return UserStatisticOverviewQuery(
            id = nn(userId),
            name = nnString(userName),
            includeAnime = type == MediaType.ANIME
        )
    }
}
