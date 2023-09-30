package com.revolgenx.anilib.setting.data.field

import com.revolgenx.anilib.UserMediaSettingMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.user.ui.model.UserOptionsModel

class SaveMediaSettingsField(
    val model: UserOptionsModel
): BaseField<UserMediaSettingMutation>() {

    override fun toQueryOrMutation(): UserMediaSettingMutation {
        return UserMediaSettingMutation(
            title = nn(model.titleLanguage),
            airingNotification = nn(model.airingNotifications),
            activityMergeTime = nn(model.activityMergeTime)
        )
    }
}