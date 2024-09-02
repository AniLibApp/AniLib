package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.ToggleFollowMutation
import com.revolgenx.anilib.common.data.field.BaseUserField

class UserToggleFollowField(id: Int) : BaseUserField<ToggleFollowMutation>() {
    init {
        this.userId = id
    }
    override fun toQueryOrMutation(): ToggleFollowMutation {
        return ToggleFollowMutation(userId = nn(userId))
    }
}