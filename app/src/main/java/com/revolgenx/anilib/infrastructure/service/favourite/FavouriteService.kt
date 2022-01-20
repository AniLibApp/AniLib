package com.revolgenx.anilib.infrastructure.service.favourite

import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

abstract class FavouriteService(val graphRepository: BaseGraphRepository) {

    //helper fun
    abstract fun isFavourite(
        mediaId: Int?,
        compositeDisposable: CompositeDisposable?,
        callback: (Resource<Boolean>) -> Unit
    )
}