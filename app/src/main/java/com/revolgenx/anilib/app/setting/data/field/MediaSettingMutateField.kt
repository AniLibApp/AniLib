package com.revolgenx.anilib.app.setting.data.field

import com.revolgenx.anilib.UserMediaSettingMutation
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.UserTitleLanguage

class MediaSettingMutateField(val model: UserOptionsModel): BaseField<UserMediaSettingMutation>(){
    override fun toQueryOrMutation(): UserMediaSettingMutation {
        return UserMediaSettingMutation.builder()
            .apply {
                with(model){
                    title(UserTitleLanguage.values()[titleLanguage])
                    airingNotification(airingNotifications)
                }
            }
            .build()
    }
}