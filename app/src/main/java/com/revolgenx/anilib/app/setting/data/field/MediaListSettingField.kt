package com.revolgenx.anilib.app.setting.data.field

import com.revolgenx.anilib.UserMediaListOptionsQuery
import com.revolgenx.anilib.user.data.field.BaseUserField

class MediaListSettingField : BaseUserField<UserMediaListOptionsQuery>() {
    override fun toQueryOrMutation(): UserMediaListOptionsQuery {
        return UserMediaListOptionsQuery.builder().apply {
            userId?.let {
                id(it)
            }
            userName?.let {
                name(it)
            }
        }.build()
    }
}

