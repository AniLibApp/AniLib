package com.revolgenx.anilib.service

import androidx.lifecycle.*
import com.revolgenx.anilib.IsFavouriteQuery
import com.revolgenx.anilib.ToggleFavouriteMutation
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ToggleServiceImpl(graphRepository: BaseGraphRepository) :
    ToggleService(graphRepository) {
    override fun toggleFavourite(
        favouriteField: ToggleFavouriteField,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<Boolean>> {
        val disposable = graphRepository.request(ToggleFavouriteMutation.builder().apply {
            if (favouriteField.animeId != null)
                animeId(favouriteField.animeId)
            if (favouriteField.mangaId != null)
                mangaId(favouriteField.mangaId)
            if (favouriteField.characterId != null)
                characterId(favouriteField.characterId)
            if (favouriteField.staffId != null)
                staffId(favouriteField.staffId)
            if (favouriteField.studioId != null)
                studioId(favouriteField.studioId)
        }.build())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                toggleFavMutableLiveData.value = Resource.success(true)
            }, {
                Timber.w(it)
                toggleFavMutableLiveData.value = Resource.error(it.message ?: ERROR, null, it)
            })
        compositeDisposable?.add(disposable)
        return toggleFavMutableLiveData
    }

    override fun isFavourite(
        mediaId: Int,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<Boolean>> {
        if ((isFavouriteLiveData.value == null)) {
            val disposable =
                graphRepository.request(IsFavouriteQuery.builder().mediaId(mediaId).build())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isFavouriteLiveData.value =
                            Resource.success(it.data()?.Media()!!.isFavourite)
                    }, {
                        Timber.w(it)
                        isFavouriteLiveData.value = Resource.error(it.message ?: ERROR, null, it)
                    })

            compositeDisposable?.add(disposable)
        }
        return isFavouriteLiveData
    }
}