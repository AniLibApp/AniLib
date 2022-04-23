package com.revolgenx.anilib.studio.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.studio.service.StudioService
import com.revolgenx.anilib.infrastructure.source.StudioMediaSource
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.field.StudioMediaField
import com.revolgenx.anilib.studio.data.model.StudioModel

class StudioViewModel(
    private val studioService: StudioService,
    private val toggleService: ToggleService
) :
    SourceViewModel<StudioMediaSource, StudioMediaField>() {

    val studioModel get() = studioInfoLiveData.value?.data
    val studioInfoLiveData = MutableLiveData<Resource<StudioModel>>()
    val toggleFavouriteLiveData = MutableLiveData<Resource<Boolean>>()

    var studioField: StudioField = StudioField()
    override var field: StudioMediaField = StudioMediaField()

    val toggleFavouriteField = ToggleFavouriteField()

    override fun createSource(): StudioMediaSource {
        source = StudioMediaSource(field, studioService, compositeDisposable)
        return source!!
    }

    fun getStudioInfo() {
        studioInfoLiveData.value = Resource.loading(null)
        studioService.getStudioInfo(studioField, compositeDisposable) {
            studioInfoLiveData.value = it
        }
    }

    fun toggleStudioFav(toggleFavouriteField: ToggleFavouriteField) {
        toggleFavouriteLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(toggleFavouriteField, compositeDisposable) {
            studioModel?.let { m->
                m.isFavourite = !m.isFavourite
            }
            toggleFavouriteLiveData.value = it
        }
    }
}