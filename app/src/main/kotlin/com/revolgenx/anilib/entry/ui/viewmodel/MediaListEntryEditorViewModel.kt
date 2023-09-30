package com.revolgenx.anilib.entry.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.entry.ui.model.AdvancedScoreModel
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

class MediaListEntryEditorViewModel(private val mediaListEntryService: MediaListEntryService) :
    ResourceViewModel<UserMediaModel, MediaListEntryField>() {
    override val field: MediaListEntryField = MediaListEntryField()

    val saveField = SaveMediaListEntryField()
    var isFavourite by mutableStateOf(false)
    var userHasMediaListEntry by mutableStateOf(false)

    override val deleteResource: MutableState<ResourceState<Any>?> = mutableStateOf(null)
    override val saveResource: MutableState<ResourceState<Any>?> = mutableStateOf(null)

    override fun load(): Flow<UserMediaModel> {
        return mediaListEntryService.getMediaListEntry(field).onEach {
            it.toSaveField()
        }
    }


    private fun UserMediaModel.toSaveField() {
        val userModel = user
        media?.let { mediaModel ->
            isFavourite = mediaModel.isFavourite
            userHasMediaListEntry = mediaModel.mediaListEntry.isNotNull()

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
                saveField.mediaId = media.id
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

    override fun save() {
        saveResource.value = ResourceState.loading()
        mediaListEntryService.saveMediaListEntry(saveField)
            .onEach {
                saveResource.value = ResourceState.success(it)
            }
            .catch {
                saveResource.value = ResourceState.error(it)
            }
            .launchIn(viewModelScope)
    }

    override fun delete() {
        val resourceValue = resource.value
        if (resourceValue is ResourceState.Success) {
            deleteResource.value = ResourceState.loading()
            resourceValue.data?.media?.mediaListEntry?.id?.let { id ->
                mediaListEntryService.deleteMediaListEntry(id)
                    .onEach {
                        if (it) {
                            deleteResource.value = ResourceState.success(it)
                        } else {
                            throw ApolloException("Failed to delete")
                        }
                    }
                    .catch {
                        deleteResource.value = ResourceState.error(it)
                    }
                    .launchIn(viewModelScope)
            }
        }
    }
}