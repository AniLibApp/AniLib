package com.revolgenx.anilib.service.recommendation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.field.media.MediaRecommendationField
import com.revolgenx.anilib.field.recommendation.AddRecommendationField
import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.field.recommendation.UpdateRecommendationField
import com.revolgenx.anilib.model.*
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
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
                    ?.filter { if (field.canShowAdult) true else it.mediaRecommendation()?.isAdult == false }
                    ?.map { node ->
                        MediaRecommendationModel().also { mod ->
                            mod.recommendationId = node.id()
                            mod.rating = node.rating()
                            mod.userRating = node.userRating()?.ordinal
                            mod.mediaId = node.media()!!.id()
                            mod.recommended = node.mediaRecommendation()?.let {
                                CommonMediaModel().also { model ->
                                    mod.mediaRecommendationId = it.id()
                                    model.mediaId = it.id()
                                    model.title = it.title()?.fragments()?.mediaTitle()?.let {
                                        TitleModel().also { title ->
                                            title.romaji = it.romaji()
                                            title.english = it.english()
                                            title.native = it.native_()
                                            title.userPreferred = it.userPreferred()
                                        }
                                    }
                                    model.seasonYear = it.seasonYear()
                                    model.status = it.status()?.ordinal
                                    model.format = it.format()?.ordinal
                                    model.type = it.type()?.ordinal
                                    model.averageScore = it.averageScore()
                                    model.coverImage =
                                        it.coverImage()?.fragments()?.mediaCoverImage()?.let {
                                            CoverImageModel().also { img ->
                                                img.medium = it.medium()
                                                img.large = it.large()
                                                img.extraLarge = it.extraLarge()
                                            }
                                        }
                                    model.bannerImage =
                                        it.bannerImage() ?: model.coverImage?.largeImage
                                }
                            }
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
                            mod.recommendationId = it.id()
                            mod.rating = it.rating()
                            mod.userRating = it.userRating()?.ordinal
                            mod.recommendationFrom =
                                it.media()!!.fragments().commonMediaContent().let {
                                    CommonMediaModel().also { model ->
                                        model.mediaId = it.id()
                                        model.title = it.title()?.fragments()?.mediaTitle()?.let {
                                            TitleModel().also { title ->
                                                title.romaji = it.romaji()
                                                title.english = it.english()
                                                title.native = it.native_()
                                                title.userPreferred = it.userPreferred()
                                            }
                                        }
                                        model.seasonYear = it.seasonYear()
                                        model.status = it.status()?.ordinal
                                        model.format = it.format()?.ordinal
                                        model.type = it.type()?.ordinal
                                        model.averageScore = it.averageScore()
                                        model.coverImage =
                                            it.coverImage()?.fragments()?.mediaCoverImage()?.let {
                                                CoverImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                    img.extraLarge = it.extraLarge()
                                                }
                                            }
                                        model.bannerImage =
                                            it.bannerImage() ?: model.coverImage?.largeImage
                                    }
                                }

                            mod.recommended =
                                it.mediaRecommendation()!!.fragments().commonMediaContent().let {
                                    CommonMediaModel().also { model ->
                                        model.mediaId = it.id()
                                        model.title = it.title()?.fragments()?.mediaTitle()?.let {
                                            TitleModel().also { title ->
                                                title.romaji = it.romaji()
                                                title.english = it.english()
                                                title.native = it.native_()
                                                title.userPreferred = it.userPreferred()
                                            }
                                        }
                                        model.seasonYear = it.seasonYear()
                                        model.status = it.status()?.ordinal
                                        model.format = it.format()?.ordinal
                                        model.type = it.type()?.ordinal
                                        model.averageScore = it.averageScore()
                                        model.coverImage =
                                            it.coverImage()?.fragments()?.mediaCoverImage()?.let {
                                                CoverImageModel().also { img ->
                                                    img.medium = it.medium()
                                                    img.large = it.large()
                                                    img.extraLarge = it.extraLarge()
                                                }
                                            }
                                        model.bannerImage =
                                            it.bannerImage() ?: model.coverImage?.largeImage
                                    }
                                }

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
                        model.recommendationId = rec.id()
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