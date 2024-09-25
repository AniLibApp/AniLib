package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ActivityInfoQuery
import com.revolgenx.anilib.common.data.field.BaseField

data class ActivityInfoField(val activityId: Int) : BaseField<ActivityInfoQuery>() {
    override fun toQueryOrMutation(): ActivityInfoQuery {
        return ActivityInfoQuery(id = nn(activityId))
    }
}