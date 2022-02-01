package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.SaveMessageActivityMutation

class SaveMessageActivityField : SaveActivityField<SaveMessageActivityMutation>() {
    var message: String
        get() = text
        set(value) {
            text = value
        }

    var private = false
    var recipientId: Int? = null

    override fun toQueryOrMutation(): SaveMessageActivityMutation {
        return SaveMessageActivityMutation(
            id = nn(id),
            _private = nn(private),
            recipientId = nn(recipientId),
            message = nn(message)
        )
    }
}