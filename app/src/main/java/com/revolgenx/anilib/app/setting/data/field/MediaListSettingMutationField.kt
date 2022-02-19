package com.revolgenx.anilib.app.setting.data.field

import com.revolgenx.anilib.UserMediaListSettingMutation
import com.revolgenx.anilib.app.setting.data.model.getRowOrderString
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.type.MediaListOptionsInput
import com.revolgenx.anilib.type.ScoreFormat

class MediaListSettingMutationField : BaseField<UserMediaListSettingMutation>() {
    var scoreFormat: Int? = null
    var rowOrder: Int? = null
    var splitCompletedAnimeListByFormat: Boolean? = null
    var splitCompletedMangaListByFormat: Boolean? = null
    var advancedScoring: MutableList<String>? = null

    var animeCustomLists: MutableList<String>? = null
    var mangaCustomLists: MutableList<String>? = null

    var advancedScoringEnabled: Boolean = false

    override fun toQueryOrMutation(): UserMediaListSettingMutation {
        val animeListOptionsInput = MediaListOptionsInput(
            splitCompletedSectionByFormat = nn(splitCompletedAnimeListByFormat),
            advancedScoring = nnList(advancedScoring),
            customLists = nnList(animeCustomLists),
            advancedScoringEnabled = nn(advancedScoringEnabled)
        )
        val mangaListOptionsInput = MediaListOptionsInput(
            splitCompletedSectionByFormat = nn(splitCompletedMangaListByFormat),
            customLists = nnList(mangaCustomLists)
        )
        return UserMediaListSettingMutation(
            animeListOptions = nn(animeListOptionsInput),
            mangaListOptions = nn(mangaListOptionsInput),
            scoreFormat = nn(scoreFormat?.let { ScoreFormat.values()[it] }),
            rowOrder = nn(rowOrder?.let { getRowOrderString(it) })
        )
    }
}