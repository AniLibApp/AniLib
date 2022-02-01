package com.revolgenx.anilib.home.recommendation.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.apollographql.apollo3.exception.ApolloHttpException
import com.revolgenx.anilib.home.recommendation.data.field.AddRecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.SaveRecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.model.MediaRecommendationModel
import com.revolgenx.anilib.media.data.model.toModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.HttpURLConnection

class RecommendationServiceImpl(graphRepository: BaseGraphRepository) :
    RecommendationService(graphRepository) {

    override fun mediaRecommendation(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<MediaRecommendationModel>>) -> Unit)
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.media?.recommendations?.nodes?.filterNotNull()
                    ?.map { node ->
                        MediaRecommendationModel().also { mod ->
                            mod.id = node.id
                            mod.rating = node.rating
                            mod.userRating = node.userRating?.ordinal
                            mod.mediaId = node.media!!.id
                            mod.recommended = node.mediaRecommendation?.mediaContent?.toModel()
                        }
                    }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                if ((it is ApolloHttpException)) {
                    if (it.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        resourceCallback.invoke(Resource.success(null))
                        return@subscribe
                    }
                }
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null))
            })
        compositeDisposable.add(disposable)
    }


    override fun recommendation(
        field: RecommendationField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<RecommendationModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.page?.recommendations
                    ?.filterNotNull()
                    ?.filter { if (field.canShowAdult) true else ((it.media?.mediaContent?.isAdult == false) and (it.mediaRecommendation?.mediaContent?.isAdult == false)) }
                    ?.map {
                        RecommendationModel().also { mod ->
                            mod.id = it.id
                            mod.rating = it.rating
                            mod.userRating = it.userRating?.ordinal
                            mod.recommendationFrom =
                                it.media?.mediaContent?.toModel()
                            mod.recommended =
                                it.mediaRecommendation?.mediaContent?.toModel()
                        }
                    }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it ?: emptyList()))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun updateRecommendation(
        recommendationField: UpdateRecommendationField,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<UpdateRecommendationModel>> {
        val disposable = graphRepository.request(recommendationField.toQueryOrMutation())
            .map {
                it.data?.saveRecommendation?.let { rec ->
                    UpdateRecommendationModel().also { model ->
                        model.id = rec.id
                        model.rating = rec.rating
                        model.userRating = rec.userRating?.ordinal
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateRecommendationLiveData.value = Resource.success(it)
            }, {
                Timber.w(it)
                updateRecommendationLiveData.value =
                    Resource.error(it.message ?: ERROR, null, it)
            })
        compositeDisposable?.add(disposable)
        return updateRecommendationLiveData
    }

    override fun removeUpdateRecommendationObserver(observer: Observer<Resource<UpdateRecommendationModel>>) {
        updateRecommendationLiveData.removeObserver(observer)
        updateRecommendationLiveData.value = null
    }

    override fun saveRecommendation(addRecommendationField: AddRecommendationField): MutableLiveData<Resource<SaveRecommendationModel>> {
        return MutableLiveData<Resource<SaveRecommendationModel>>()
    }

}