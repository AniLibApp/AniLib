package com.revolgenx.anilib.data.field.setting

import com.revolgenx.anilib.UserMediaOptionsQuery
import com.revolgenx.anilib.UserMediaSettingMutation
import com.revolgenx.anilib.data.field.BaseField
import com.revolgenx.anilib.data.field.user.BaseUserField
import com.revolgenx.anilib.data.model.setting.MediaOptionModel
import com.revolgenx.anilib.type.UserTitleLanguage

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

class MediaSettingMutateField(val model:MediaOptionModel):BaseField<UserMediaSettingMutation>(){
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