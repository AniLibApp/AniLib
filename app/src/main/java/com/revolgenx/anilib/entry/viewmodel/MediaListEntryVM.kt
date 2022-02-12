package com.revolgenx.anilib.entry.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.model.UserMediaModel
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

class MediaListEntryVM(
    private val mediaListEntryService: MediaListEntryService
) : BaseViewModel() {
    val mediaListEntry = MutableLiveData<Resource<UserMediaModel>>()
    val saveMediaListEntry = MutableLiveData<Resource<MediaListModel>>()
    val deleteMediaListEntry = MutableLiveData<Resource<Boolean>>()

    val field = MediaListEntryField().also {
        it.userId = UserPreference.userId
    }

    val saveField = SaveMediaListEntryField()

    fun getMediaListEntry() {
        mediaListEntry.value = Resource.loading(null)
        mediaListEntryService.getMediaListEntry(field, compositeDisposable) {
            it.data?.let { presetDataIfNotPresent(it) }
            mediaListEntry.value = it
        }
    }

    private fun presetDataIfNotPresent(userMediaModel: UserMediaModel) {
        userMediaModel.media?.mediaListEntry?.toSaveField() ?: with(saveField) {
            status = MediaListStatus.CURRENT.ordinal
            score = 0.0
            userMediaModel.user?.mediaListOptions?.let { mediaListOptionModel ->
                if (userMediaModel.media!!.type == MediaType.ANIME.ordinal) {
                    mediaListOptionModel.animeList?.let { list ->
                        advancedScores = list.advancedScoring?.map {
                            MutablePair(
                                it,
                                0.0
                            )
                        }
                        customLists = list.customLists?.map { MutablePair(it, false) }
                    }
                } else {
                    mediaListOptionModel.mangaList?.let { list ->
                        advancedScores = list.advancedScoring?.map {
                            MutablePair(
                                it,
                                0.0
                            )
                        }
                        customLists = list.customLists?.map { MutablePair(it, false) }
                    }
                }
            }
        }
    }

    fun saveMediaListEntry() {
        if (saveField.id == null && saveField.mediaId == null) return
        saveMediaListEntry.value = Resource.loading(null)
        mediaListEntryService.saveMediaListEntry(saveField, compositeDisposable) {
            //TODO LATER in fragment
//            mediaListCollectionStoreVM.update(it)
            saveMediaListEntry.value = it
        }
    }

    fun deleteMediaListEntry() {
        if (saveField.id == null) return
        saveMediaListEntry.value = Resource.loading(null)
        mediaListEntryService.deleteMediaListEntry(saveField.id, compositeDisposable) {
            //TODO LATER in fragment
//            mediaListCollectionStoreVM.delete(it)
            deleteMediaListEntry.value = it
        }
    }

    private fun MediaListModel.toSaveField() {
        saveField.id = id
        saveField.mediaId = mediaId
        saveField.progress = progress
        saveField.progressVolumes = progressVolumes
        saveField.advancedScores = advanceScores
        saveField.customLists = customLists
        saveField.startedAt = startedAt
        saveField.private = private
        saveField.hiddenFromStatusLists = hiddenFromStatusLists
        saveField.notes = notes
        saveField.repeat = repeat
        saveField.status = status
        saveField.score = score
        saveField.completedAt = completedAt
    }
}