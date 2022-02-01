package com.revolgenx.anilib.app.setting.data.field

import com.revolgenx.anilib.UserMediaListOptionsQuery
import com.revolgenx.anilib.user.data.field.BaseUserField

class MediaListSettingField : BaseUserField<UserMediaListOptionsQuery>() {
    override fun toQueryOrMutation(): UserMediaListOptionsQuery {
        return UserMediaListOptionsQuery(id = nn(userId), name = nn(userName))
    }
}

