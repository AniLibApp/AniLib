package com.revolgenx.anilib.media.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.media.data.model.MediaRecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.app.setting.store.TranslationStore
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
import com.revolgenx.anilib.infrastructure.source.MediaOverviewRecommendationSource
import com.revolgenx.anilib.util.CommonTimer
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.media.data.model.MediaModel

class MediaOverviewVM(
    private val mediaInfoService: MediaInfoService,
    private val recommendationService: RecommendationService
) : SourceViewModel<MediaOverviewRecommendationSource, MediaRecommendationField>() {

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    val mediaRecommendedList by lazy {
        mutableMapOf<Int, MediaRecommendationModel>()
    }

    val translationStore = TranslationStore()

    val mediaOverviewLiveData = MutableLiveData<Resource<MediaModel>>()

    override var field: MediaRecommendationField = MediaRecommendationField()

    fun getOverview(field: MediaOverviewField) {
        mediaOverviewLiveData.value = Resource.loading(null)
        mediaInfoService.getMediaOverview(field, compositeDisposable) {
            mediaOverviewLiveData.value = it.also { res ->
                res.data?.nextAiringEpisode?.let {
                    it.commonTimer = CommonTimer(handler, it.timeUntilAiringModel!!)
                }
            }
        }
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
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }

}