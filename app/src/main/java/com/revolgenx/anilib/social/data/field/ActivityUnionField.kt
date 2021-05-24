package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.data.field.BaseSourceField
import com.revolgenx.anilib.type.ActivityType

class ActivityUnionField:BaseSourceField<ActivityUnionQuery>() {

    var isFollowing:Boolean? = null
    var type:Int? = ActivityType.TEXT.ordinal

    override fun toQueryOrMutation(): ActivityUnionQuery {
        return ActivityUnionQuery.builder()
            .page(page)
            .perPage(perPage)
            .apply {
                isFollowing?.let {
                    isFollowing(it)
                }
                type?.let {
                    type(ActivityType.values()[it])
                }
            }
            .build()
    }
}