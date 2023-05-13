package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.type.ScoreFormat


data class MediaListOptionModel(
    val scoreFormat: ScoreFormat? = null,
    val rowOrder: Int? = null,
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
