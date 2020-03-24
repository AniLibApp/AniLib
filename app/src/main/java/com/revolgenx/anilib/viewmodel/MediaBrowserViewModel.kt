package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.model.MediaBrowseMediaModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.MediaBrowseService
import com.revolgenx.anilib.service.ToggleService
import io.reactivex.disposables.CompositeDisposable

class MediaBrowserViewModel(
    private val mediaBrowseService: MediaBrowseService,
    private val toggleService: ToggleService
) : ViewModel() {
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    val mediaLiveData by lazy {
        MediatorLiveData<Resource<MediaBrowseMediaModel>>().also {
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

    fun getMediaInfo(mediaId: Int): LiveData<Resource<MediaBrowseMediaModel>> {
        return mediaBrowseService.getSimpleMedia(mediaId, compositeDisposable)
    }

    fun isFavourite(mediaId: Int): MutableLiveData<Resource<Boolean>> {
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