package com.revolgenx.anilib.setting.data.field

import com.revolgenx.anilib.UserMediaListOptionsQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class MediaListSettingsField: BaseUserField<UserMediaListOptionsQuery>() {
    override fun toQueryOrMutation(): UserMediaListOptionsQuery {
        return UserMediaListOptionsQuery(id = nn(userId), name = nn(userName))
    }
}
