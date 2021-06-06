package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.DeleteActivityMutation
import com.revolgenx.anilib.data.field.BaseField

class ActivityDeleteField:BaseField<DeleteActivityMutation>() {
    var activityId:Int? = null
    override fun toQueryOrMutation(): DeleteActivityMutation {
        return DeleteActivityMutation.builder().id(activityId).build()
    }
}