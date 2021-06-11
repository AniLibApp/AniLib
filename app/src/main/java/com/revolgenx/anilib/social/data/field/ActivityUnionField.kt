package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.data.field.BaseSourceField
import com.revolgenx.anilib.data.meta.type.AlActivityType
import com.revolgenx.anilib.type.ActivityType

class ActivityUnionField : BaseSourceField<ActivityUnionQuery>() {

    var isFollowing: Boolean = false
    var type: AlActivityType = AlActivityType.ALL

    override fun toQueryOrMutation(): ActivityUnionQuery {
        return ActivityUnionQuery.builder()
            .page(page)
            .perPage(perPage)
            .apply {
                if (isFollowing) {
                    isFollowing(true)
                }
                when (type) {
                    AlActivityType.TEXT -> type(ActivityType.TEXT)
                    AlActivityType.LIST -> type(ActivityType.MEDIA_LIST)
                    else -> type_in(listOf(ActivityType.MEDIA_LIST, ActivityType.TEXT, ActivityType.ANIME_LIST, ActivityType.MANGA_LIST))
                }
            }
            .build()
    }

}
