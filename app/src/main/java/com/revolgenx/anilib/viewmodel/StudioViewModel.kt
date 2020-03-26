package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.field.studio.StudioField
import com.revolgenx.anilib.field.studio.StudioMediaField
import com.revolgenx.anilib.model.studio.StudioModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.ToggleService
import com.revolgenx.anilib.service.studio.StudioService
import com.revolgenx.anilib.source.StudioMediaSource

class StudioViewModel(
    private val studioService: StudioService,
    private val toggleService: ToggleService
) :
    SourceViewModel<StudioMediaSource, StudioMediaField>() {

    val studioInfoLiveData by lazy {
        MediatorLiveData<Resource<StudioModel>>().also {
            it.addSource(studioService.studioInfoLivData) { res ->
                it.value = res
            }
        }
    }

    val toggleFavouriteLiveData by lazy {
        MediatorLiveData<Resource<Boolean>>().also {
            it.addSource(toggleService.toggleFavMutableLiveData) { res ->
                it.value = res
            }
        }
    }

    override fun createSource(field: StudioMediaField): StudioMediaSource {
        source =  StudioMediaSource(field, studioService, compositeDisposable)
        return source!!
    }

    fun getStudioInfo(field: StudioField) {
        studioInfoLiveData.value = Resource.loading(null)
        studioService.getStudioInfo(field, compositeDisposable)
    }

    fun toggleStudioFav(toggleFavouriteField: ToggleFavouriteField) {
        toggleFavouriteLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(toggleFavouriteField, compositeDisposable)
    }
}
