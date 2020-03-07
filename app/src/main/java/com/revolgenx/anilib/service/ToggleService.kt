package com.revolgenx.anilib.service

import androidx.lifecycle.*
import com.revolgenx.anilib.ToggleFavouriteMutation
import com.revolgenx.anilib.model.ToggleFavouriteModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class ToggleService(val graphRepository: BaseGraphRepository) {
    val toggleFavMutableLiveData = MutableLiveData<Resource<Boolean>>()
    val isFavouriteLiveData = MutableLiveData<Resource<Boolean>>()
    abstract fun toggleFavourite(favouriteModel: ToggleFavouriteModel, compositeDisposable: CompositeDisposable? = null): MutableLiveData<Resource<Boolean>>

    //helper fun
    abstract fun isFavourite(mediaId:Int,compositeDisposable: CompositeDisposable?):MutableLiveData<Resource<Boolean>>
}