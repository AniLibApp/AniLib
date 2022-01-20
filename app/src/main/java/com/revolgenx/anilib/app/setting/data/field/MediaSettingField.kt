package com.revolgenx.anilib.app.setting.data.field

import com.revolgenx.anilib.UserMediaOptionsQuery
import com.revolgenx.anilib.user.data.field.BaseUserField

class MediaSettingField : BaseUserField<UserMediaOptionsQuery>() {
    override fun toQueryOrMutation(): UserMediaOptionsQuery {
        return UserMediaOptionsQuery.builder().apply {
            userId?.let {
                id(it)
            }
            userName?.let {
                name(it)
            }
        }.build()
    }
}

