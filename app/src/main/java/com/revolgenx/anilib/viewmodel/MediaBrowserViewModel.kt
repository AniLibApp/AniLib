package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.model.ToggleFavouriteModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.ToggleService
import io.reactivex.disposables.CompositeDisposable

class MediaBrowserViewModel(private val toggleService: ToggleService) :ViewModel(){
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    val toggleFavMediaLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().apply {
            addSource(toggleService.toggleFavMutableLiveData) {
                this.value = it
            }
        }
    }

    fun toggleMediaFavourite(toggleFavouriteModel: ToggleFavouriteModel) {
        toggleFavMediaLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(
            toggleFavouriteModel,
            compositeDisposable
        )
    }


}