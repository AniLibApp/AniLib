package com.revolgenx.anilib.infrastructure.service.toggle

import com.revolgenx.anilib.ToggleActivitySubscriptionMutation
import com.revolgenx.anilib.ToggleLikeV2Mutation
import com.revolgenx.anilib.data.model.toggle.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
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
                it.data()?.ToggleLikeV2()?.let {
                    when (it) {
                        is ToggleLikeV2Mutation.AsTextActivity -> {
                            LikeableUnionModel().also { model ->
                                model.id = it.id()
                                model.isLiked = it.isLiked ?: false
                                model.likeCount = it.likeCount()
                            }
                        }
                        is ToggleLikeV2Mutation.AsListActivity -> {
                            LikeableUnionModel().also { model ->
                                model.id = it.id()
                                model.isLiked = it.isLiked ?: false
                                model.likeCount = it.likeCount()

                            }

                        }
                        is ToggleLikeV2Mutation.AsActivityReply -> {

                            LikeableUnionModel().also { model ->
                                model.id = it.id()
                                model.isLiked = it.isLiked ?: false
                                model.likeCount = it.likeCount()

                            }
                        }
                        is ToggleLikeV2Mutation.AsMessageActivity -> {

                            LikeableUnionModel().also { model ->
                                model.id = it.id()
                                model.isLiked = it.isLiked ?: false
                                model.likeCount = it.likeCount()

                            }
                        }
                        else -> {
                            null
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
                it.data()?.ToggleActivitySubscription()?.let {
                    when (it) {
                        is ToggleActivitySubscriptionMutation.AsTextActivity -> {
                            TextActivityModel().also { model ->
                                model.id = it.id()
                                model.isSubscribed = it.isSubscribed ?: false
                            }
                        }
                        is ToggleActivitySubscriptionMutation.AsListActivity -> {
                            ListActivityModel().also { model ->
                                model.id = it.id()
                                model.isSubscribed = it.isSubscribed ?: false
                            }
                        }
                        else -> {
                            null
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

}