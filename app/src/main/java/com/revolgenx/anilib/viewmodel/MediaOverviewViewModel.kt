package com.revolgenx.anilib.viewmodel

import android.os.Handler
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.field.media.MediaOverviewField
import com.revolgenx.anilib.field.media.MediaRecommendationField
import com.revolgenx.anilib.field.recommendation.UpdateRecommendationField
import com.revolgenx.anilib.model.MediaOverviewModel
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.media.MediaBrowseService
import com.revolgenx.anilib.service.recommendation.RecommendationService
import com.revolgenx.anilib.source.MediaOverviewRecommendationSource
import com.revolgenx.anilib.util.CommonTimer

class MediaOverviewViewModel(
    private val mediaBrowseService: MediaBrowseService,
    private val recommendationService: RecommendationService
) : SourceViewModel<MediaOverviewRecommendationSource, MediaRecommendationField>() {

    private val handler by lazy {
        Handler()
    }

    val mediaRecommendedList by lazy {
        mutableMapOf<Int, MediaRecommendationModel>()
    }


    val mediaOverviewLiveData by lazy {
        MediatorLiveData<Resource<MediaOverviewModel>>().apply {
            addSource(mediaBrowseService.mediaOverviewLiveData) { res ->
                res.data?.airingTimeModel?.let {
                    it.commonTimer = CommonTimer(handler, it.airingTime!!)
                }
                this.value = res
            }
        }
    }

    override var field: MediaRecommendationField = MediaRecommendationField()

    fun getOverview(field: MediaOverviewField) {
        mediaOverviewLiveData.value = Resource.loading(null)
        mediaBrowseService.getMediaOverview(field, compositeDisposable)
    }


    fun removeUpdateRecommendationObserver(observer: Observer<Resource<UpdateRecommendationModel>>) {
        recommendationService.removeUpdateRecommendationObserver(observer)
    }


    fun updateRecommendation(field: UpdateRecommendationField): MutableLiveData<Resource<UpdateRecommendationModel>> {
        return recommendationService.updateRecommendation(field, compositeDisposable)
    }

    override fun createSource(): MediaOverviewRecommendationSource {
        source = MediaOverviewRecommendationSource(
            recommendationService,
            compositeDisposable,
            field
        )
        return source!!
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }

}