package com.revolgenx.anilib.setting.data.field

import com.revolgenx.anilib.UserMediaOptionsQuery
import com.revolgenx.anilib.common.data.field.BaseUserField

class MediaSettingsField: BaseUserField<UserMediaOptionsQuery>() {
    override fun toQueryOrMutation(): UserMediaOptionsQuery {
        return UserMediaOptionsQuery(id = nn(userId), name = nnString(userName))
    }
}