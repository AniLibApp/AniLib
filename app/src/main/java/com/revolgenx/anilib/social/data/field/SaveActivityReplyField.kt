package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveActivityReplyMutation

class SaveActivityReplyField : SaveActivityField<SaveActivityReplyMutation>() {
    var activityId:Int? = null
    override fun toQueryOrMutation(): SaveActivityReplyMutation {
        return SaveActivityReplyMutation.builder()
            .apply {
                id?.let {
                    id(id)
                }
                activityId(activityId)
            }
            .text(text)
            .build()
    }
}