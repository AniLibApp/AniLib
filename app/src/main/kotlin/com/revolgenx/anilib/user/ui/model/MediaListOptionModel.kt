package com.revolgenx.anilib.user.ui.model

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

enum class RowOrder {
    SCORE, TITLE, LAST_UPDATED, LAST_ADDED
}

fun getRowOrder(rowOrder: String?): RowOrder {
    return when (rowOrder) {
        "score" -> {
            RowOrder.SCORE
        }
        "title" -> {
            RowOrder.TITLE
        }
        "updatedAt" -> {
            RowOrder.LAST_UPDATED
        }
        else -> {
            RowOrder.LAST_ADDED
        }
    }
}