package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.fragment.UserMediaListOptions
import com.revolgenx.anilib.type.ScoreFormat


data class MediaListOptionModel(
    val scoreFormat: ScoreFormat? = null,
    val rowOrder: RowOrder? = null,
    val animeList: MediaListOptionTypeModel? = null,
    val mangaList: MediaListOptionTypeModel? = null,
) {

    val isAnimeAdvancedScoreEnabled = (scoreFormat == ScoreFormat.POINT_10_DECIMAL
            || scoreFormat == ScoreFormat.POINT_100)
            && animeList?.advancedScoringEnabled == true

    val isMangaAdvancedScoreEnabled = (scoreFormat == ScoreFormat.POINT_10_DECIMAL
            || scoreFormat == ScoreFormat.POINT_100)
            && mangaList?.advancedScoringEnabled == true

}

enum class RowOrder(val value: String) {
    SCORE("score"), TITLE("title"), LAST_UPDATED("updatedAt"), LAST_ADDED("id");

    companion object {
        fun fromValue(value: String?): RowOrder? {
            return entries.find { it.value == value }
        }
    }
}

fun getRowOrder(rowOrder: String?): RowOrder {
    return RowOrder.fromValue(rowOrder) ?: RowOrder.LAST_ADDED
}


fun UserMediaListOptions.toModel() = MediaListOptionModel(
    rowOrder = getRowOrder(rowOrder ?: RowOrder.TITLE.value),
    scoreFormat = scoreFormat ?: ScoreFormat.POINT_100,
    animeList = animeList?.let {
        MediaListOptionTypeModel(
            advancedScoringEnabled = it.advancedScoringEnabled == true,
            advancedScoring =
            it.advancedScoring?.filterNotNull()?.toMutableList(),
            customLists = it.customLists?.filterNotNull(),
            splitCompletedSectionByFormat = it.splitCompletedSectionByFormat == true
        )
    },
    mangaList = mangaList?.let {
        MediaListOptionTypeModel(
            advancedScoringEnabled = it.advancedScoringEnabled == true,
            advancedScoring =
            it.advancedScoring?.filterNotNull()?.toMutableList(),
            customLists = it.customLists?.filterNotNull(),
            splitCompletedSectionByFormat = it.splitCompletedSectionByFormat == true
        )
    }
)