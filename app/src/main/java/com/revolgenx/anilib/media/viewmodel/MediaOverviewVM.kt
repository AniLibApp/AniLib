package com.revolgenx.anilib.media.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.revolgenx.anilib.media.data.field.MediaOverviewField
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.app.setting.store.TranslationStore
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.home.recommendation.service.RecommendationService
import com.revolgenx.anilib.infrastructure.source.MediaOverviewRecommendationSource
import com.revolgenx.anilib.util.CommonTimer
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.type.RecommendationRating
import kotlinx.coroutines.flow.onEach

class MediaOverviewVM(
    private val mediaInfoService: MediaInfoService,
    private val recommendationService: RecommendationService
) : SourceViewModel<MediaOverviewRecommendationSource, MediaRecommendationField>() {

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    val mediaRecommendedList by lazy {
        mutableMapOf<Int, RecommendationModel>()
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

    fun upVoteRecommendation(item: RecommendationModel) =
        saveRecommendation(item, RecommendationRating.RATE_UP.ordinal)

    fun downVoteRecommendation(item: RecommendationModel) =
        saveRecommendation(item, RecommendationRating.RATE_DOWN.ordinal)

    fun saveRecommendation(
        item: RecommendationModel,
        rating: Int
    ): LiveData<Resource<RecommendationModel>> {

        val saveField = SaveRecommendationField()
        saveField.mediaId = item.recommendedFromId
        saveField.mediaRecommendationId = item.recommended?.id

        saveField.rating = if (item.userRating == rating) {
            RecommendationRating.NO_RATING.ordinal
        } else {
            rating
        }

        return recommendationService.saveRecommendation(saveField).onEach {
            it.data?.let {
                item.id = it.id
                item.userRating = it.userRating
                item.rating = it.rating
            }
        }.asLiveData()
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