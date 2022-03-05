package com.revolgenx.anilib.home.recommendation.service

import com.apollographql.apollo3.exception.ApolloHttpException
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.data.field.MediaRecommendationField
import com.revolgenx.anilib.media.data.model.toModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.net.HttpURLConnection

class RecommendationServiceImpl(private val graphRepository: BaseGraphRepository) :
    RecommendationService {

    override fun getMediaRecommendations(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<RecommendationModel>>) -> Unit)
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.media?.recommendations?.nodes?.filterNotNull()
                    ?.map { node ->
                        RecommendationModel().also { mod ->
                            mod.id = node.id
                            mod.rating = node.rating
                            mod.userRating = node.userRating?.ordinal
                            mod.recommendedFromId = node.media?.id
                            mod.recommended = node.mediaRecommendation?.mediaContent?.toModel()
                        }
                    }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                if ((it is ApolloHttpException)) {
                    if (it.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        callback.invoke(Resource.success(null))
                        return@subscribe
                    }
                }
                Timber.e(it)
                callback.invoke(Resource.error(it.message, null))
            })
        compositeDisposable.add(disposable)
    }


    override fun getRecommendations(
        field: RecommendationField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<RecommendationModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.page?.recommendations?.mapNotNull {
                    it?.takeIf {
                        if (field.canShowAdult) true else ((it.media?.mediaContent?.isAdult == false)
                                and (it.mediaRecommendation?.mediaContent?.isAdult == false))
                    }
                        ?.let {
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
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it ?: emptyList()))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun saveRecommendation(
        recommendationField: SaveRecommendationField
    ): Flow<Resource<RecommendationModel>> {
        return graphRepository.mutation(recommendationField.toQueryOrMutation())
            .map {
                Resource.success(
                    it.data?.saveRecommendation?.let {
                        RecommendationModel().also { model ->
                            model.id = it.id
                            model.userRating = it.userRating?.ordinal
                            model.rating = it.rating
                        }
                    }
                )
            }.catch {
                emit(Resource.error(it))
            }
    }

}