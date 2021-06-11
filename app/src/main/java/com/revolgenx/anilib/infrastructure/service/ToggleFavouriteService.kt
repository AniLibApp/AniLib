package com.revolgenx.anilib.infrastructure.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

abstract class ToggleFavouriteService(val graphRepository: BaseGraphRepository) {
    val toggleFavMutableLiveData = MutableLiveData<Resource<Boolean>>()
    val isFavouriteLiveData = MutableLiveData<Resource<Boolean>>()
    abstract fun toggleFavourite(favouriteField: ToggleFavouriteField, compositeDisposable: CompositeDisposable? = null): MutableLiveData<Resource<Boolean>>

    //helper fun
    abstract fun isFavourite(mediaId:Int?,compositeDisposable: CompositeDisposable?):MutableLiveData<Resource<Boolean>>
}