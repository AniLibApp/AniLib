package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserStatisticOverviewQuery
import com.revolgenx.anilib.type.MediaType

class UserStatisticOverviewField : BaseUserField<UserStatisticOverviewQuery>() {
    var type: Int = MediaType.ANIME.ordinal
    override fun toQueryOrMutation(): UserStatisticOverviewQuery {
        return UserStatisticOverviewQuery(
            id = nn(userId),
            name = nn(userName),
            includeAnime = type == MediaType.ANIME.ordinal
        )
    }
}
