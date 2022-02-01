package com.revolgenx.anilib.infrastructure.service.toggle

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.ToggleFavouriteMutation
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.social.data.model.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ToggleServiceImpl(private val graphRepository: BaseGraphRepository) :
    ToggleService {
    override fun toggleLikeV2(
        field: ToggleLikeV2Field,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<LikeableUnionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.toggleLikeV2?.let {
                    it.onTextActivity?.let {
                        LikeableUnionModel().also { model ->
                            model.id = it.id
                            model.isLiked = it.isLiked ?: false
                            model.likeCount = it.likeCount
                        }
                    }

                    it.onListActivity?.let {
                        LikeableUnionModel().also { model ->
                            model.id = it.id
                            model.isLiked = it.isLiked ?: false
                            model.likeCount = it.likeCount

                        }
                    }
                    it.onActivityReply?.let {
                        LikeableUnionModel().also { model ->
                            model.id = it.id
                            model.isLiked = it.isLiked ?: false
                            model.likeCount = it.likeCount
                        }
                    }
                    it.onMessageActivity?.let {
                        LikeableUnionModel().also { model ->
                            model.id = it.id
                            model.isLiked = it.isLiked ?: false
                            model.likeCount = it.likeCount
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


    override fun toggleActivitySubscription(
        field: ToggleActivitySubscriptionField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ActivityUnionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.toggleActivitySubscription?.let {
                    it.onTextActivity?.let {
                        TextActivityModel().also { model ->
                            model.id = it.id
                            model.isSubscribed = it.isSubscribed ?: false
                        }
                    }
                    it.onListActivity?.let {
                        ListActivityModel().also { model ->
                            model.id = it.id
                            model.isSubscribed = it.isSubscribed ?: false
                        }
                    }

                    it.onMessageActivity?.let {
                        MessageActivityModel().also { model ->
                            model.id = it.id
                            model.isSubscribed = it.isSubscribed ?: false
                        }
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it))
            }, {
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun toggleFavourite(
        field: ToggleFavouriteField,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(ToggleFavouriteMutation(
            animeId = Optional.presentIfNotNull(field.animeId),
            mangaId = Optional.presentIfNotNull(field.mangaId),
            characterId = Optional.presentIfNotNull(field.characterId),
            staffId = Optional.presentIfNotNull(field.staffId),
            studioId = Optional.presentIfNotNull(field.studioId),
        ))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ ->
                callback.invoke(Resource.success(true))
            }, {
                Timber.e(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable?.add(disposable)
    }
}