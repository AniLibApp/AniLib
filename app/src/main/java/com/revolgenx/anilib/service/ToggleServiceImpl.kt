package com.revolgenx.anilib.service

import androidx.lifecycle.*
import com.revolgenx.anilib.ToggleFavouriteMutation
import com.revolgenx.anilib.model.ToggleFavouriteModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ToggleServiceImpl(graphRepository: BaseGraphRepository) :
    ToggleService(graphRepository) {
    override fun toggleFavourite(
        favouriteModel: ToggleFavouriteModel,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<Boolean>> {
        val disposable = graphRepository.request(ToggleFavouriteMutation.builder().apply {
            if (favouriteModel.animeId != null)
                animeId(favouriteModel.animeId)
            if (favouriteModel.mangaId != null)
                mangaId(favouriteModel.mangaId)
            if (favouriteModel.characterId != null)
                characterId(favouriteModel.characterId)
            if (favouriteModel.staffId != null)
                staffId(favouriteModel.staffId)
            if (favouriteModel.studioId != null)
                studioId(favouriteModel.studioId)
        }.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                toggleFavMutableLiveData.value = Resource.success(true)
            }, {
                Timber.e(it)
                toggleFavMutableLiveData.value = Resource.error(it.message ?: "Error", null)
            })
        compositeDisposable?.add(disposable)
        return toggleFavMutableLiveData
    }
}