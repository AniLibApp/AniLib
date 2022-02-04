package com.revolgenx.anilib.infrastructure.service.favourite

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.IsFavouriteQuery
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class FavouriteServiceImpl(graphRepository: BaseGraphRepository) :
    FavouriteService(graphRepository) {
    override fun isFavourite(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val disposable =
            graphRepository.request(IsFavouriteQuery(mediaId = Optional.presentIfNotNull(mediaId)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.invoke(Resource.success(it.data?.media!!.isFavourite))
                }, {
                    Timber.e(it)
                    callback.invoke(Resource.error(it.message ?: ERROR, null, it))
                })
        compositeDisposable?.add(disposable)
    }
}