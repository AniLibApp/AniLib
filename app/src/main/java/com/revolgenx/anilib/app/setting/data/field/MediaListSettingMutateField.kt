package com.revolgenx.anilib.app.setting.data.field

import com.revolgenx.anilib.UserMediaListSettingMutation
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.getRowOrderString
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.MediaListOptionsInput
import com.revolgenx.anilib.type.ScoreFormat

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