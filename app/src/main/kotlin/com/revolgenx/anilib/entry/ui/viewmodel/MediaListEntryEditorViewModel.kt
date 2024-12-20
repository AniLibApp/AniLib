package com.revolgenx.anilib.entry.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.entry.ui.model.AdvancedScoreModel
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.list.data.store.MediaListEntryEventStore
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.ScoreFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlin.math.roundToInt

class MediaListEntryEditorViewModel(
    private val mediaListEntryService: MediaListEntryService,
    private val mediaService: MediaService,
    private val appPreferencesDataStore: AppPreferencesDataStore,
    private val mediaListEntryEventStore: MediaListEntryEventStore
) :
    ResourceViewModel<UserMediaModel, MediaListEntryField>() {


    override val field: MediaListEntryField = MediaListEntryField().also {
        it.userId = appPreferencesDataStore.userId.get()
    }

    val isLoggedIn = appPreferencesDataStore.isLoggedIn.get()
    val saveField = SaveMediaListEntryField()
    var isFavourite by mutableStateOf(false)
    var userHasMediaListEntry by mutableStateOf(false)

    override fun load(): Flow<UserMediaModel> {
        return mediaListEntryService.getMediaListEntry(field).onEach {
            it.toSaveField()
        }
    }


    private fun UserMediaModel.toSaveField() {
        val userModel = user
        media?.let { mediaModel ->
            isFavourite = mediaModel.isFavourite.value
            userHasMediaListEntry = mediaModel.mediaListEntry.isNotNull()

            mediaModel.mediaListEntry?.apply {
                saveField.id = id
                saveField.mediaId = mediaId
                saveField.progress = progress
                saveField.progressVolumes = progressVolumes
                saveField.customLists = customLists
                saveField.startedAt = startedAt
                saveField.private = private
                saveField.hiddenFromStatusLists = hiddenFromStatusLists
                saveField.notes = notes
                saveField.repeat = repeat
                saveField.status = status.value
                saveField.score = score
                saveField.completedAt = completedAt
                saveField.advancedScores = advancedScores
            } ?: let {
                saveField.mediaId = media.id
                saveField.status = MediaListStatus.CURRENT
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
                            mutableDoubleStateOf(0.0)
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
        super.save()
        val currentMedia = getData()?.media
        currentMedia?.mediaListEntry?.let { currentEntry ->
            val currentProgress = currentEntry.progress
            val initialProgress = currentProgress == null || currentProgress == 0
            val newProgress = saveField.progress
            val currentStatus = currentEntry.status.value

            if (saveField.status == MediaListStatus.PLANNING && currentStatus == MediaListStatus.PLANNING && initialProgress && newProgress != null && newProgress > 0) {
                saveField.status = MediaListStatus.CURRENT
            }

            if(saveField.status == MediaListStatus.CURRENT && currentStatus == MediaListStatus.CURRENT && newProgress != null && newProgress == currentMedia.totalEpisodesOrChapters){
                saveField.status = MediaListStatus.COMPLETED
            }
        }
        mediaListEntryService.saveMediaListEntry(saveField)
            .onEach { listModel ->
                saveComplete(listModel)
                if (listModel != null) {
                    if (userHasMediaListEntry) {
                        mediaListEntryEventStore.update(listModel)
                    } else {
                        mediaListEntryEventStore.create(listModel)
                    }
                }
            }
            .catch {
                saveFailed(it)
            }
            .launchIn(viewModelScope)
    }

    override fun delete() {
        val resourceValue = getData() ?: return
        super.delete()
        resourceValue.media?.mediaListEntry?.let { entry ->
            mediaListEntryService.deleteMediaListEntry(entry.id)
                .onEach {
                    if (it) {
                        deleteComplete(it)
                        mediaListEntryEventStore.delete(entry)
                    } else {
                        throw ApolloException("Failed to delete")
                    }
                }
                .catch {
                    deleteFailed(it)
                }
                .launchIn(viewModelScope)
        }
    }


    fun toggleFavourite() {
        val mediaId = field.mediaId
        val type = getData()?.media?.type
        if (mediaId == -1 || type == null) return

        isFavourite = !isFavourite

        launch {
            val toggled = mediaService.toggleFavourite(mediaId = mediaId, type = type).single()
            if (!toggled) {
                isFavourite = !isFavourite
                showOperationFailedMsg()
            }
        }
    }


    private fun showOperationFailedMsg() {
        errorMsg = anilib.i18n.R.string.operation_failed
    }
}