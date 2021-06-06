package com.revolgenx.anilib.infrastructure.service.toggle

import com.revolgenx.anilib.ToggleLikeV2Mutation
import com.revolgenx.anilib.data.model.toggle.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ToggleLikeV2ServiceImpl(private val graphRepository: BaseGraphRepository) :
    ToggleLikeV2Service {
    override fun toggleLike(
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
}