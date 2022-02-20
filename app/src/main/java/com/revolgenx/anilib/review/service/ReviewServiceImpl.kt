package com.revolgenx.anilib.review.service

import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloHttpException
import com.revolgenx.anilib.DeleteReviewMutation
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.network.converter.toModel
import com.revolgenx.anilib.common.repository.util.ERROR
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.review.data.field.AllReviewField
import com.revolgenx.anilib.review.data.field.RateReviewField
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.SaveReviewField
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.data.model.toModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

class ReviewServiceImpl(private val graphRepository: BaseGraphRepository) : ReviewService {
    override fun getReview(
        field: ReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ReviewModel?>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation()).map {
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
                    model.userId = it.userId
                    model.user = it.user?.let {
                        UserModel().also { user ->
                            user.id = it.id
                            user.name = it.name
                            user.avatar = it.avatar?.userAvatar?.toModel()
                        }
                    }
                    model.mediaId = it.mediaId
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
                callback.invoke(Resource.success(it))
            }, {
                if (it is ApolloHttpException) {
                    if (it.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        callback.invoke(Resource.success(null))
                        return@subscribe
                    }
                }
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
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
                        model.user = it.user?.let {
                            UserModel().also { user ->
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
        field: SaveReviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Int>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { it.data?.saveReview?.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun deleteReview(
        id: Int?,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(
            DeleteReviewMutation(
                reviewId = Optional.presentIfNotNull(id)
            )
        ).map { it.data?.deleteReview?.deleted == true }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
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