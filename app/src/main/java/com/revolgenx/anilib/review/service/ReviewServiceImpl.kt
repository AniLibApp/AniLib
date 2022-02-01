package com.revolgenx.anilib.review.service

import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.exception.ApolloHttpException
import com.revolgenx.anilib.DeleteReviewMutation
import com.revolgenx.anilib.ReviewQuery
import com.revolgenx.anilib.SaveReviewMutation
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.review.data.field.AllReviewField
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.user.data.model.UserAvatarModel
import com.revolgenx.anilib.user.data.model.UserPrefModel
import com.revolgenx.anilib.user.data.model.toModel
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
            it.data?.review?.let {
                ReviewModel().also { model ->
                    model.id = it.id
                    model.rating = it.rating
                    model.ratingAmount = it.ratingAmount
                    model.userRating = it.userRating?.ordinal
                    model.summary = it.summary
                    model.body = it.body ?: ""
                    model.score = it.score
                    model.private = it.private == true
                    model.createdAtDate = it.createdAt.let {
                        SimpleDateFormat.getDateInstance().format(Date(it * 1000L))
                    }
                    model.userPrefModel = it.user?.let {
                        UserPrefModel().also { user ->
                            user.id = it.id
                            user.name = it.name
                            user.avatar = it.avatar?.userAvatar?.toModel()
                        }
                    }
                    model.media = it.media?.let {
                        MediaModel().also { media ->
                            media.id = it.id
                            media.title = it.title?.mediaTitle?.toModel()
                            media.coverImage = it.coverImage?.mediaCoverImage?.toModel()
                            media.bannerImage = it.bannerImage
                            media.type = it.type?.ordinal
                        }
                    }
                }
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                reviewLiveData.value = Resource.success(it)
            }, {
                if (it is ApolloHttpException) {
                    if (it.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
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
            it.data?.page?.reviews
                ?.filterNotNull()
                ?.filter { if (field.canShowAdult) true else it.media?.isAdult == false }?.map {
                    ReviewModel().also { model ->
                        model.id = it.id
                        model.rating = it.rating
                        model.ratingAmount = it.ratingAmount
                        model.summary = it.summary
                        model.score = it.score
                        model.createdAtDate = it.createdAt.let {
                            SimpleDateFormat.getDateInstance().format(Date(it * 1000L))
                        }
                        model.userPrefModel = it.user?.let {
                            UserPrefModel().also { user ->
                                user.id = it.id
                                user.name = it.name
                                user.avatar = it.avatar?.userAvatar?.toModel()
                            }
                        }
                        model.media = it.media?.let {
                            MediaModel().also { media ->
                                media.id = it.id
                                media.title = it.title?.mediaTitle?.toModel()
                                media.coverImage =
                                    it.coverImage?.mediaCoverImage?.toModel()
                                media.bannerImage = it.bannerImage ?: media.coverImage?.largeImage
                                media.type = it.type?.ordinal
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
            it.data?.rateReview?.let {
                ReviewModel().also { model ->
                    model.id = it.id
                    model.userRating = it.userRating?.ordinal
                    model.ratingAmount = it.ratingAmount
                    model.rating = it.rating
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