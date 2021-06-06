package com.revolgenx.anilib.ui.viewmodel.media

import android.os.Handler
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.data.field.media.MediaOverviewField
import com.revolgenx.anilib.data.field.media.MediaRecommendationField
import com.revolgenx.anilib.data.field.recommendation.UpdateRecommendationField
import com.revolgenx.anilib.data.model.media_info.MediaOverviewModel
import com.revolgenx.anilib.data.model.media_info.MediaRecommendationModel
import com.revolgenx.anilib.data.model.recommendation.UpdateRecommendationModel
import com.revolgenx.anilib.data.store.TranslationStore
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.service.recommendation.RecommendationService
import com.revolgenx.anilib.infrastructure.source.MediaOverviewRecommendationSource
import com.revolgenx.anilib.util.CommonTimer
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

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

    val translationStore = TranslationStore()

    val mediaOverviewLiveData by lazy {
        MediatorLiveData<Resource<MediaOverviewModel>>().apply {
            addSource(mediaBrowseService.mediaOverviewLiveData) { res ->
                res.data?.airingTimeModel?.let {
                    it.commonTimer = CommonTimer(handler, it.timeUntilAiring!!)
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