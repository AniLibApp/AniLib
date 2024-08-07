package com.revolgenx.anilib.setting.data.field

import com.revolgenx.anilib.UserMediaListSettingMutation
import com.revolgenx.anilib.common.data.field.BaseUserField
import com.revolgenx.anilib.type.MediaListOptionsInput
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.user.ui.model.RowOrder

data class SaveMediaListSettingsField(
    val scoreFormat: ScoreFormat?,
    val rowOrder: RowOrder?,
    val splitCompletedAnimeListByFormat: Boolean?,
    val splitCompletedMangaListByFormat: Boolean?,
    val advancedScoring: List<String>?,
    val animeCustomLists: List<String>?,
    val mangaCustomLists: List<String>?,
    val advancedScoringEnabled: Boolean,
) : BaseUserField<UserMediaListSettingMutation>() {

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
            scoreFormat = nn(scoreFormat),
            rowOrder = nn(rowOrder?.value)
        )
    }
}
