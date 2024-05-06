package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveActivityReplyMutation
import com.revolgenx.anilib.common.data.field.BaseField

data class SaveActivityReplyField(
    val replyId: Int? = null,
    val activityId: Int? = null,
    val text: String
) : BaseField<SaveActivityReplyMutation>(){
    override fun toQueryOrMutation(): SaveActivityReplyMutation {
        return SaveActivityReplyMutation(
            id = nn(replyId),
            activityId = nn(activityId),
            text = nn(text)
        )
    }
}