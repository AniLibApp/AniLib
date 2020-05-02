package com.revolgenx.anilib.field.user

import com.revolgenx.anilib.ToggleFollowMutation

class UserToggleFollowField : BaseUserField<ToggleFollowMutation>() {

    override fun toQueryOrMutation(): ToggleFollowMutation {
        return ToggleFollowMutation.builder()
            .userId(userId)
            .build()
    }

}
