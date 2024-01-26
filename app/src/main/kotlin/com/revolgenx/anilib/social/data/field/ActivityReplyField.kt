package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityReplyQuery
import com.revolgenx.anilib.common.data.field.BaseSourceField

data class ActivityReplyField(
    var activityId: Int = -1
) : BaseSourceField<ActivityReplyQuery>() {
    override fun toQueryOrMutation(): ActivityReplyQuery {
        return ActivityReplyQuery(
            page = nn(page),
            perPage = nn(perPage),
            activityId = nn(activityId)
        )
    }
}