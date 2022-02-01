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

        val options = MediaListOptionsInput(
            advancedScoring = nn(model.animeList!!.advancedScoring!!.map { it.scoreType }),
            advancedScoringEnabled = nn(model.animeList!!.advancedScoringEnabled),
        )
        return UserMediaListSettingMutation(
            mediaListOptions = nn(options),
            scoreFormat = nn(ScoreFormat.values()[model.scoreFormat!!]),
            rowOrder = nn(getRowOrderString(model.rowOrder!!))
        )
    }
}