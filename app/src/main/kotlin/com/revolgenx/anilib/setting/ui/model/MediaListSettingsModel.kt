package com.revolgenx.anilib.setting.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.RowOrder

data class MediaListSettingsModel(
    val scoreFormat: MutableState<ScoreFormat>,
    val rowOrder: MutableState<RowOrder>,
    val advancedScoringEnabled: MutableState<Boolean>,
    val advancedScoring: SnapshotStateList<String>,
    val animeCustomLists: SnapshotStateList<String>,
    val mangaCustomLists: SnapshotStateList<String>,
    val splitCompletedAnimeSectionByFormat: MutableState<Boolean>,
    val splitCompletedMangaSectionByFormat: MutableState<Boolean>,
    val animeCustomListText: MutableState<String> = mutableStateOf(""),
    val mangaCustomListText: MutableState<String> = mutableStateOf(""),
    val advancedScoringText: MutableState<String> = mutableStateOf(""),
    val showAdvancedScoring: State<Boolean> = derivedStateOf {
        (scoreFormat.value == ScoreFormat.POINT_10_DECIMAL
                || scoreFormat.value == ScoreFormat.POINT_100)
    },
){

}

fun MediaListOptionModel.toModel(): MediaListSettingsModel {
    return MediaListSettingsModel(
        scoreFormat = mutableStateOf(scoreFormat ?: ScoreFormat.POINT_100),
        rowOrder = mutableStateOf(rowOrder ?: RowOrder.TITLE),
        advancedScoringEnabled = mutableStateOf(animeList?.advancedScoringEnabled == true),
        advancedScoring = mutableStateListOf(*animeList?.advancedScoring?.toTypedArray() ?: arrayOf()),
        animeCustomLists = mutableStateListOf(*animeList?.customLists?.toTypedArray() ?: arrayOf()),
        mangaCustomLists = mutableStateListOf(*mangaList?.customLists?.toTypedArray() ?: arrayOf()),
        splitCompletedAnimeSectionByFormat = mutableStateOf(animeList?.splitCompletedSectionByFormat == true),
        splitCompletedMangaSectionByFormat = mutableStateOf(mangaList?.splitCompletedSectionByFormat == true),
    )
}