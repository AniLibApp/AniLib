package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.UserStatisticOverviewQuery
import com.revolgenx.anilib.type.MediaType

class UserStatisticOverviewField : BaseUserField<UserStatisticOverviewQuery>() {
    var type: Int = MediaType.ANIME.ordinal
    override fun toQueryOrMutation(): UserStatisticOverviewQuery {
        return UserStatisticOverviewQuery.builder()
            .apply {
                userId?.let {
                    id(it)
                }
                userName?.let {
                    name(it)
                }
                includeAnime(type == MediaType.ANIME.ordinal)
            }.build()
    }
}
