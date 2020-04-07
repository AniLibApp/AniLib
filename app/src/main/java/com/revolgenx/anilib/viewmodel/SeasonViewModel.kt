package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.*
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.model.season.SeasonMediaModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.source.SeasonSource
import io.reactivex.disposables.CompositeDisposable

class SeasonViewModel(private val repository: BaseGraphRepository) : ViewModel() {
    var seasonSource: SeasonSource? = null
    private val compositeDisposable = CompositeDisposable()

    val seasonMediaList = mutableMapOf<Int, SeasonMediaModel>()

    fun createSource(seasonField: SeasonField): SeasonSource {
        seasonSource = SeasonSource(
            baseGraphRepository = repository,
            seasonField = seasonField,
            compositeDisposable = compositeDisposable
        )
        return seasonSource!!
    }

    override fun onCleared() {
        compositeDisposable.clear()
        seasonMediaList.clear()
        super.onCleared()
    }

}
