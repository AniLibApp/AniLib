package com.revolgenx.anilib.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.model.field.overview.MediaRecommendationField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

abstract class RecommendationService(protected var graphRepository: BaseGraphRepository) {
    val mediaRecommendationLiveData
            by lazy { MutableLiveData<Resource<List<MediaRecommendationModel>>>() }

    abstract fun mediaRecommendation(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable? = null
    ): MutableLiveData<Resource<List<MediaRecommendationModel>>>

}