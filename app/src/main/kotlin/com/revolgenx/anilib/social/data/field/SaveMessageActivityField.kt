package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveMessageActivityMutation
import com.revolgenx.anilib.common.data.field.BaseField

data class SaveMessageActivityField(
    val activityId: Int? = null,
    val message: String,
    val private: Boolean,
    val recipientId: Int
) : BaseField<SaveMessageActivityMutation>() {
    override fun toQueryOrMutation(): SaveMessageActivityMutation {
        return SaveMessageActivityMutation(
            id = nn(activityId),
            _private = nn(private),
            recipientId = nn(recipientId),
            message = nn(message)
        )
    }
}