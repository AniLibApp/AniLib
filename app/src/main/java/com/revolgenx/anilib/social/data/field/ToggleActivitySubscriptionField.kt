package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.ToggleActivitySubscriptionMutation
import com.revolgenx.anilib.common.data.field.BaseField

class ToggleActivitySubscriptionField : BaseField<ToggleActivitySubscriptionMutation>() {
    var activityId: Int? = null
    var isSubscribed:Boolean = false
    override fun toQueryOrMutation(): ToggleActivitySubscriptionMutation {
        return ToggleActivitySubscriptionMutation.builder()
            .activityId(activityId)
            .subscribe(isSubscribed)
            .build()
    }
}