package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.model.MediaOverviewModel
import com.revolgenx.anilib.field.MediaOverviewField
import com.revolgenx.anilib.field.UpdateRecommendationField
import com.revolgenx.anilib.field.overview.MediaRecommendationField
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.MediaBrowseService
import com.revolgenx.anilib.service.RecommendationService
import com.revolgenx.anilib.source.BrowserOverviewRecommendationSource
import io.reactivex.disposables.CompositeDisposable

class MediaOverviewViewModel(
    private val mediaBrowseService: MediaBrowseService,
    private val recommendationService: RecommendationService
) : ViewModel() {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    var browserOverviewRecommendationSource: BrowserOverviewRecommendationSource? = null
    val mediaOverviewLiveData by lazy {
        MediatorLiveData<Resource<MediaOverviewModel>>().apply {
            addSource(mediaBrowseService.mediaOverviewLiveData) {
                this.value = it
            }
        }
    }

    fun getOverview(field: MediaOverviewField) {
        mediaOverviewLiveData.value = Resource.loading(null)
        mediaBrowseService.getMediaOverview(field, compositeDisposable)
    }

    fun createRecommendationSource(
        field: MediaRecommendationField,
        lifecycleOwner: LifecycleOwner
    ): BrowserOverviewRecommendationSource {
        browserOverviewRecommendationSource = BrowserOverviewRecommendationSource(
            recommendationService,
            field,
            lifecycleOwner,
            compositeDisposable
        )
        return browserOverviewRecommendationSource!!
    }


    fun updateRecommendation(field: UpdateRecommendationField): MutableLiveData<Resource<UpdateRecommendationModel>> {
        return recommendationService.updateRecommendation(field, compositeDisposable)
    }

}