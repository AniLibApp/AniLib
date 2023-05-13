package com.revolgenx.anilib.entry.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.entry.ui.model.AdvancedScoreModel
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

class MediaListEntryEditorViewModel(private val mediaListEntryService: MediaListEntryService) :
    ResourceViewModel<UserMediaModel, MediaListEntryField>() {
    override val field: MediaListEntryField = MediaListEntryField()

    val saveField = SaveMediaListEntryField()

    override fun loadData(): Flow<UserMediaModel> {
        return mediaListEntryService.getMediaListEntry(field).onEach {
            it.toSaveField()
        }
    }


    private fun UserMediaModel.toSaveField() {
        val userModel = user
        media?.let { mediaModel ->
            mediaModel.mediaListEntry?.apply {
                saveField.id = id
                saveField.mediaId = mediaId
                saveField.progress = progress
                saveField.progressVolumes = progressVolumes
                saveField.customLists = customLists
                saveField.startedAt = startedAt
                saveField.private = private ?: false
                saveField.hiddenFromStatusLists = hiddenFromStatusLists
                saveField.notes = notes
                saveField.repeat = repeat
                saveField.status = status ?: MediaListStatus.CURRENT
                saveField.score = score ?: 0.0
                saveField.completedAt = completedAt
                saveField.advancedScores = advancedScores
            } ?: let {
                val listOptions = userModel?.mediaListOptions?.let { mediaListOptionModel ->
                    if (mediaModel.isAnime) {
                        mediaListOptionModel.animeList
                    } else {
                        mediaListOptionModel.mangaList
                    }
                }
                saveField.advancedScores =
                    listOptions?.advancedScoring?.map {
                        AdvancedScoreModel(
                            it,
                            mutableStateOf(0.0)
                        )
                    }
                saveField.customLists =
                    listOptions?.customLists?.map { MutablePair(it, false) }
            }
        }
    }

    fun onAdvancedScoreChange(scoreFormat: ScoreFormat) {
        saveField.advancedScores?.let { advancedScoring ->
            val meanScore = advancedScoring.sumOf { it.score.value }
                .div(advancedScoring.count { it.score.value != 0.0 }.takeIf { it != 0 } ?: 1)
                .let {
                    (it * 10).roundToInt() / 10.0
                }
            val max = if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) {
                10.0
            } else {
                100.0
            }
            saveField.score = if (meanScore > max) max else meanScore
        }
    }
}