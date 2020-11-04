package com.revolgenx.anilib.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.field.studio.StudioField
import com.revolgenx.anilib.data.field.studio.StudioMediaField
import com.revolgenx.anilib.data.model.studio.StudioModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.ToggleService
import com.revolgenx.anilib.infrastructure.service.studio.StudioService
import com.revolgenx.anilib.infrastructure.source.StudioMediaSource

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
    var studioField: StudioField = StudioField()
    override var field: StudioMediaField = StudioMediaField()
    override fun createSource(): StudioMediaSource {
        source = StudioMediaSource(field, studioService, compositeDisposable)
        return source!!
    }

    fun getStudioInfo() {
        studioInfoLiveData.value = Resource.loading(null)
        studioService.getStudioInfo(studioField, compositeDisposable)
    }

    fun toggleStudioFav(toggleFavouriteField: ToggleFavouriteField) {
        toggleFavouriteLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(toggleFavouriteField, compositeDisposable)
    }
}
