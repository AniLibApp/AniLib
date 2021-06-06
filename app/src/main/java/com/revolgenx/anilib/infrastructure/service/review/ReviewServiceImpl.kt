package com.revolgenx.anilib.infrastructure.service.review

import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.exception.ApolloHttpException
import com.revolgenx.anilib.DeleteReviewMutation
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.SaveReviewMutation
import com.revolgenx.anilib.data.field.review.AllReviewField
import com.revolgenx.anilib.data.field.review.RateReviewField
import com.revolgenx.anilib.data.field.review.ReviewField
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.data.model.user.UserPrefModel
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.user.AvatarModel
import com.revolgenx.anilib.data.model.review.ReviewModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

class ReviewServiceImpl(private val graphRepository: BaseGraphRepository) : ReviewService {

    override val reviewLiveData: MutableLiveData<Resource<ReviewModel>> = MutableLiveData()

    override fun getReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation() as ReviewQuery).map {
            it.data()?.Review()?.let {
                ReviewModel().also { model ->
                    model.reviewId = it.id()
                    model.rating = it.rating()
                    model.ratingAmount = it.ratingAmount()
                    model.userRating = it.userRating()?.ordinal
                    model.summary = it.summary()
                    model.body = MarkwonImpl.createMarkwonCompatible(it.body() ?: "")
                    model.score = it.score()
                    model.private = it.private_()
                    model.createdAt = it.createdAt().let {
                        SimpleDateFormat.getDateInstance().format(Date(it * 1000L))
                    }
                    model.userPrefModel = it.user()?.let {
                        UserPrefModel().also { user ->
                            user.id = it.id()
                            user.name = it.name()
                            user.avatar = it.avatar()?.let {
                                AvatarModel().also { img ->
                                    img.large = it.large()
                                    img.medium = it.medium()
                                }
                            }
                        }
                    }
                    model.mediaModel = it.media()?.let {
                        CommonMediaModel().also { media ->
                            media.mediaId = it.id()
                            media.title = it.title()?.fragments()?.mediaTitle()?.toModel()
                            media.coverImage =
                                it.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
                            media.bannerImage = it.bannerImage()
                            media.type = it.type()?.ordinal
                        }
                    }
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                reviewLiveData.value = Resource.success(it)
            }, {
                if (it is ApolloHttpException) {
                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        reviewLiveData.value = Resource.success(null)
                        return@subscribe
                    }
                }
                Timber.e(it)
                reviewLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })
        compositeDisposable.add(disposable)
    }


    override fun getAllReview(
        field: AllReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<ReviewModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.Page()?.reviews()
                ?.filter { if (field.canShowAdult) true else it.media()?.isAdult == false }?.map {
                    ReviewModel().also { model ->
                        model.reviewId = it.id()
                        model.rating = it.rating()
                        model.ratingAmount = it.ratingAmount()
                        model.summary = it.summary()
                        model.score = it.score()
                        model.createdAt = it.createdAt().let {
                            SimpleDateFormat.getDateInstance().format(Date(it * 1000L))
                        }
                        model.userPrefModel = it.user()?.let {
                            UserPrefModel().also { user ->
                                user.id = it.id()
                                user.name = it.name()
                                user.avatar = it.avatar()?.let {
                                    AvatarModel().also { img ->
                                        img.large = it.large()
                                        img.medium = it.medium()
                                    }
                                }
                            }
                        }
                        model.mediaModel = it.media()?.let {
                            CommonMediaModel().also { media ->
                                media.mediaId = it.id()
                                media.title = it.title()?.fragments()?.mediaTitle()?.toModel()
                                media.coverImage =
                                    it.coverImage()?.fragments()?.mediaCoverImage()?.toModel()
                                media.bannerImage = it.bannerImage() ?: media.coverImage?.largeImage
                                media.type = it.type()?.ordinal
                                media.isAdult = it.isAdult ?: false
                            }
                        }
                    }
                }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }


    override fun saveReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation() as SaveReviewMutation)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(true))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, false, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun deleteReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation() as DeleteReviewMutation)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(true))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, false, it))
            })
        compositeDisposable.add(disposable)
    }


    override fun rateReview(
        field: RateReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ReviewModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation()).map {
            it.data()?.RateReview()?.let {
                ReviewModel().also { model ->
                    model.reviewId = it.id()
                    model.userRating = it.userRating()?.ordinal
                    model.ratingAmount = it.ratingAmount()
                    model.rating = it.rating()
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it?.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}