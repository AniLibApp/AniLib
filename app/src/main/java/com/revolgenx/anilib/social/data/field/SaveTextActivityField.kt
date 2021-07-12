package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveTextActivityMutation

class SaveTextActivityField:SaveActivityField<SaveTextActivityMutation>() {
    override fun toQueryOrMutation(): SaveTextActivityMutation {
        return SaveTextActivityMutation.builder()
            .apply {
                id?.let {
                    id(it)
                }
            }
            .text(text).build()
    }
}