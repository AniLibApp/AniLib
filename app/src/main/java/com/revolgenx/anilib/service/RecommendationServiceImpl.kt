package com.revolgenx.anilib.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.model.SaveRecommendationModel
import com.revolgenx.anilib.field.AddRecommendationField
import com.revolgenx.anilib.field.UpdateRecommendationField
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.model.TitleModel
import com.revolgenx.anilib.field.overview.MediaRecommendationField
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.util.pmap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RecommendationServiceImpl(graphRepository: BaseGraphRepository) :
    RecommendationService(graphRepository) {

    override fun mediaRecommendation(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<List<MediaRecommendationModel>>> {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                runBlocking {
                    it.data()?.Media()?.recommendations()?.nodes()?.pmap { node ->
                        MediaRecommendationModel().also { mediaRecommendationModel ->
                            mediaRecommendationModel.recommendationId = node.id()
                            mediaRecommendationModel.mediaId = node.media()!!.id()
                            mediaRecommendationModel.mediaRecommendationId =
                                node.mediaRecommendation()!!.id()
                            mediaRecommendationModel.averageScore =
                                node.mediaRecommendation()!!.averageScore()
                            mediaRecommendationModel.rating = node.rating()
                            mediaRecommendationModel.userRating = node.userRating()?.ordinal
                            mediaRecommendationModel.type = node.mediaRecommendation()!!.type()!!.ordinal
                            mediaRecommendationModel.format =
                                node.mediaRecommendation()?.format()?.ordinal
                            mediaRecommendationModel.seasonYear =
                                node.mediaRecommendation()?.seasonYear()
                            mediaRecommendationModel.episodes =
                                node.mediaRecommendation()?.episodes()?.toString()
                            mediaRecommendationModel.chapters = node.mediaRecommendation()?.chapters()?.toString()
                            mediaRecommendationModel.status =
                                node.mediaRecommendation()?.status()?.ordinal
                            mediaRecommendationModel.coverImage =
                                node.mediaRecommendation()?.coverImage()?.fragments()
                                    ?.mediaCoverImage()?.let {
                                        CoverImageModel().also { imageModel ->
                                            imageModel.extraLarge = it.extraLarge()
                                            imageModel.large = it.large()
                                            imageModel.medium = it.medium()
                                        }
                                    }
                            mediaRecommendationModel.bannerImage =
                                node.mediaRecommendation()?.bannerImage()
                                    ?: mediaRecommendationModel.coverImage?.extraLarge

                            mediaRecommendationModel.title =
                                node.mediaRecommendation()?.title()?.fragments()?.mediaTitle()
                                    ?.let { title ->
                                        TitleModel().also { titleModel ->
                                            titleModel.english = title.english() ?: title.romaji()
                                            titleModel.romaji = title.romaji()
                                            titleModel.native = title.native_()
                                            titleModel.userPreferred = title.userPreferred()
                                        }
                                    }
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mediaRecommendationLiveData.value = Resource.success(it)
            }, {
                Timber.w(it)
                mediaRecommendationLiveData.value = Resource.error(it.message ?: ERROR, null)
            })
        compositeDisposable?.add(disposable)
        return mediaRecommendationLiveData
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
                if (it is ApolloHttpException) {
                    val code = it.code()
                    updateRecommendationLiveData.value =
                        Resource.error(it.message ?: ERROR, null, code)
                } else {
                    updateRecommendationLiveData.value = Resource.error(it.message ?: ERROR, null)
                }
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