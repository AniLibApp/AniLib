package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ToggleActivitySubscriptionMutation
import com.revolgenx.anilib.common.data.field.BaseField


data class ToggleActivitySubscriptionField(
    val activityId: Int,
    val isSubscribed: Boolean
) : BaseField<ToggleActivitySubscriptionMutation>() {
    override fun toQueryOrMutation(): ToggleActivitySubscriptionMutation {
        return ToggleActivitySubscriptionMutation(
            activityId = nn(activityId),
            subscribe = nn(isSubscribed)
        )
    }
}