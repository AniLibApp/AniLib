package com.revolgenx.anilib.entry.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.entry.data.field.MediaListEntryField
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.data.model.UserMediaModel
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

class MediaListEntryVM(
    private val mediaListEntryService: MediaListEntryService,
    private val toggleService: ToggleService
) : BaseViewModel() {
    val mediaListEntry = MutableLiveData<Resource<UserMediaModel>>()
    val saveMediaListEntry = MutableLiveData<Resource<MediaListModel>>()
    val deleteMediaListEntry = MutableLiveData<Resource<Boolean>>()
    val favouriteLiveData = MutableLiveData<Resource<Boolean>>()

    val userMediaModel get() = mediaListEntry.value?.data
    val media get() = userMediaModel?.media
    val user get() = userMediaModel?.user

    val field = MediaListEntryField().also {
        it.userId = UserPreference.userId
    }

    val saveField = SaveMediaListEntryField()
    val toggleFavouriteField = ToggleFavouriteField()

    fun getMediaListEntry() {
        mediaListEntry.value = Resource.loading(null)
        mediaListEntryService.getMediaListEntry(field, compositeDisposable) {
            it.data?.let { presetDataIfNotPresent(it) }
            mediaListEntry.value = it
        }
    }

    private fun presetDataIfNotPresent(userMediaModel: UserMediaModel) {
        userMediaModel.media?.let { media ->
            if (media.isAnime()) {
                toggleFavouriteField.animeId = media.id
            } else {
                toggleFavouriteField.mangaId = media.id
            }
            media.mediaListEntry?.toSaveField() ?: with(saveField) {
                status = MediaListStatus.CURRENT.ordinal
                score = 0.0
                userMediaModel.user?.mediaListOptions?.let { mediaListOptionModel ->
                    if (media.isAnime()) {
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
    }

    private fun MediaModel.isAnime() = type == MediaType.ANIME.ordinal

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

    fun toggleFavourite() {
        val media = media ?: return
        media.isFavourite = media.isFavourite.not()
        favouriteLiveData.value = Resource.success(media.isFavourite)
        toggleService.toggleFavourite(toggleFavouriteField, compositeDisposable) {
            if (it.status == Status.ERROR) {
                media.isFavourite = media.isFavourite.not()
                favouriteLiveData.value = it
            }
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