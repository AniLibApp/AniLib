package com.revolgenx.anilib.media.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.media.data.model.isAnime

class MediaInfoContainerSharedVM(
    private val toggleService: ToggleService,
    private val entryService: MediaListEntryService
) : BaseViewModel() {
    val mediaLiveData = MutableLiveData<Resource<MediaModel>>()
    var onFavouriteChanged: ((Resource<Boolean>) -> Unit)? = null
    var onListEntryDataChanged: ((Resource<MediaListModel>) -> Unit)? = null
    val mediaModel get() = mediaLiveData.value?.data
    val toggleFavouriteField = ToggleFavouriteField()

    fun toggleFavourite() {
        val media = mediaModel ?: return
        media.isFavourite = media.isFavourite.not()
        onFavouriteChanged?.invoke(Resource.success(media.isFavourite))
        if (isAnime(mediaModel)) {
            toggleFavouriteField.animeId = media.id
        } else {
            toggleFavouriteField.mangaId = media.id
        }
        toggleService.toggleFavourite(toggleFavouriteField, compositeDisposable) {
            if (it is Resource.Error) {
                media.isFavourite = media.isFavourite.not()
                onFavouriteChanged?.invoke(it)
            }
        }
    }

    fun changeListEntryStatus(status: Int) {
        val media = mediaModel ?: return
        val saveEntryField = SaveMediaListEntryField().also {
            it.mediaId = media.id
            it.status = status
        }

        entryService.saveMediaListEntry(saveEntryField, compositeDisposable) {
            it.data?.let {
                mediaModel?.mediaListEntry?.status = it.status
            }
            onListEntryDataChanged?.invoke(it)
        }
    }
}