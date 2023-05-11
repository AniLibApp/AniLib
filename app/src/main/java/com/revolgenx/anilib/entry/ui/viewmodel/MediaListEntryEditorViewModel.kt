package com.revolgenx.anilib.entry.ui.viewmodel

import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.service.MediaListEntryService
import com.revolgenx.anilib.entry.ui.model.UserMediaModel
import com.revolgenx.anilib.type.MediaListStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

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
                saveField.private = private
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
                    listOptions?.advancedScoring?.map { MutablePair(it, 0.0) }
                saveField.customLists =
                    listOptions?.customLists?.map { MutablePair(it, false) }
            }
        }
    }
}