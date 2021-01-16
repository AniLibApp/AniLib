package com.revolgenx.anilib.data.field.setting

import com.revolgenx.anilib.UserMediaListOptionsQuery
import com.revolgenx.anilib.UserMediaListSettingMutation
import com.revolgenx.anilib.data.field.BaseField
import com.revolgenx.anilib.data.field.user.BaseUserField
import com.revolgenx.anilib.data.model.setting.MediaListOptionModel
import com.revolgenx.anilib.data.model.setting.getRowOrderString
import com.revolgenx.anilib.type.MediaListOptionsInput
import com.revolgenx.anilib.type.ScoreFormat

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

class MediaListSettingMutateField(val model: MediaListOptionModel) :
    BaseField<UserMediaListSettingMutation>() {
    override fun toQueryOrMutation(): UserMediaListSettingMutation {
        return UserMediaListSettingMutation.builder()
            .apply {
                val options = MediaListOptionsInput.builder()
                options.advancedScoring(model.animeList!!.advancedScoring!!.map { it.scoreType })
                options.advancedScoringEnabled(model.animeList!!.advancedScoringEnabled)
                mediaListOptions(options.build())
                scoreFormat(ScoreFormat.values()[model.scoreFormat!!])
                rowOrder(getRowOrderString(model.rowOrder!!))
            }
            .build()
    }
}