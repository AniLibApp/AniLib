package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.SeasonListQuery
import com.revolgenx.anilib.model.field.SeasonField
import com.revolgenx.anilib.repository.network.GraphRepository
import com.revolgenx.anilib.source.SeasonSource

class SeasonViewModel(private val repository: GraphRepository) : ViewModel() {
    lateinit var seasonSource: SeasonSource

    fun createSource(seasonField: SeasonField): SeasonSource {
        seasonSource = SeasonSource(baseGraphRepository = repository, seasonField = seasonField)
        return seasonSource
    }
}
