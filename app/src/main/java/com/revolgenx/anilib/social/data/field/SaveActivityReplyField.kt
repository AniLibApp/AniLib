package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveActivityReplyMutation
import com.revolgenx.anilib.data.field.BaseField

class SaveActivityReplyField : SaveActivityField<SaveActivityReplyMutation>() {
    override fun toQueryOrMutation(): SaveActivityReplyMutation {
        return SaveActivityReplyMutation.builder()
            .apply {
                id?.let {
                    id(id)
                }
            }
            .text(text)
            .build()
    }
}