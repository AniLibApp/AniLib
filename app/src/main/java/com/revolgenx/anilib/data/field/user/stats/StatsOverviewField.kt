package com.revolgenx.anilib.data.field.user.stats

import com.revolgenx.anilib.StatsOverviewQuery
import com.revolgenx.anilib.data.field.user.BaseUserField
import com.revolgenx.anilib.type.MediaType

class StatsOverviewField : BaseUserField<StatsOverviewQuery>() {
    var type: Int = MediaType.ANIME.ordinal
    override fun toQueryOrMutation(): StatsOverviewQuery {
        return StatsOverviewQuery.builder()
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
