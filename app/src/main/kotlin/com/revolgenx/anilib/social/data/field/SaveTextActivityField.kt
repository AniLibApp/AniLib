package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveTextActivityMutation
import com.revolgenx.anilib.common.data.field.BaseField

data class SaveTextActivityField(
    val activityId: Int? = null,
    val text: String
) : BaseField<SaveTextActivityMutation>() {
    override fun toQueryOrMutation(): SaveTextActivityMutation {
        return SaveTextActivityMutation(
            id = nn(activityId),
            text = nn(text)
        )
    }
}


