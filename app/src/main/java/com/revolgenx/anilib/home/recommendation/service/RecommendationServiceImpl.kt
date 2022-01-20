package com.revolgenx.anilib.home.recommendation.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.home.recommendation.data.field.AddRecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.SaveRecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.getCommonMedia
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.model.MediaRecommendationModel
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
                it.data()?.Media()?.recommendations()?.nodes()
                    ?.map { node ->
                        MediaRecommendationModel().also { mod ->
                            mod.id = node.id()
                            mod.rating = node.rating()
                            mod.userRating = node.userRating()?.ordinal
                            mod.mediaId = node.media()!!.id()
                            mod.recommended = node.mediaRecommendation()?.fragments()?.narrowMediaContent()?.getCommonMedia(
                                CommonMediaModel()
                            )
                        }
                    }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                if ((it is ApolloHttpException)) {
                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
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
                it.data()?.Page()?.recommendations()
                    ?.filter { if (field.canShowAdult) true else ((it.media()?.fragments()?.commonMediaContent()?.isAdult == false) and (it.mediaRecommendation()?.fragments()?.commonMediaContent()?.isAdult == false)) }
                    ?.map {
                        RecommendationModel().also { mod ->
                            mod.id = it.id()
                            mod.rating = it.rating()
                            mod.userRating = it.userRating()?.ordinal
                            mod.recommendationFrom =
                                it.media()!!.fragments().commonMediaContent().getCommonMedia(
                                    CommonMediaModel()
                                )
                            mod.recommended =
                                it.mediaRecommendation()!!.fragments().commonMediaContent().getCommonMedia(
                                    CommonMediaModel()
                                )
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
                it.data()?.SaveRecommendation()!!.let { rec ->
                    UpdateRecommendationModel().also { model ->
                        model.id = rec.id()
                        model.rating = rec.rating()
                        model.userRating = rec.userRating()?.ordinal
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