package com.revolgenx.anilib.ui.viewmodel.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.data.model.MediaBrowseModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.ToggleService
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.service.media.MediaListEntryService
import io.reactivex.disposables.CompositeDisposable

class MediaBrowserViewModel(
    private val mediaBrowseService: MediaBrowseService,
    private val toggleService: ToggleService,
    private val mediaListEntryService: MediaListEntryService
) : ViewModel() {
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    val mediaLiveData by lazy {
        MediatorLiveData<Resource<MediaBrowseModel>>().also {
            it.addSource(mediaBrowseService.simpleMediaLiveData) { res ->
                it.value = res
            }
        }
    }

    val toggleFavMediaLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().apply {
            addSource(toggleService.toggleFavMutableLiveData) {
                this.value = it
            }
        }
    }

    val saveMediaListEntryLiveData by lazy {
        MediatorLiveData<Resource<EntryListEditorMediaModel>>().apply {
            addSource(mediaListEntryService.saveMediaListEntryLiveData) {
                this.value = it
            }
        }
    }

    fun saveMediaListEntry(model: EntryListEditorMediaModel) {
        saveMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.saveMediaListEntry(
            model,
            compositeDisposable
        )
    }

    fun getMediaInfo(mediaId: Int?): LiveData<Resource<MediaBrowseModel>> {
        return mediaBrowseService.getSimpleMedia(mediaId, compositeDisposable)
    }

    fun isFavourite(mediaId: Int?): MutableLiveData<Resource<Boolean>> {
        return toggleService.isFavourite(mediaId, compositeDisposable)
    }


    fun toggleMediaFavourite(toggleFavouriteField: ToggleFavouriteField) {
        toggleFavMediaLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(
            toggleFavouriteField,
            compositeDisposable
        )
    }
}