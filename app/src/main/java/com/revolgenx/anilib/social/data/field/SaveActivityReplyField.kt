package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveActivityReplyMutation

class SaveActivityReplyField : SaveActivityField<SaveActivityReplyMutation>() {
    var activityId:Int? = null
    override fun toQueryOrMutation(): SaveActivityReplyMutation {
        return SaveActivityReplyMutation(id = nn(id), activityId = nn(activityId), text = nn(text))
    }
}