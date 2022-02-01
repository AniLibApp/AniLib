package com.revolgenx.anilib.user.data.field

import com.revolgenx.anilib.ToggleFollowMutation

class UserToggleFollowField : BaseUserField<ToggleFollowMutation>() {

    override fun toQueryOrMutation(): ToggleFollowMutation {
        return ToggleFollowMutation(userId = nn(userId))
    }

}
