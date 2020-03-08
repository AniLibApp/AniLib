package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.*
import com.revolgenx.anilib.model.field.SeasonField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.source.SeasonSource

class SeasonViewModel(private val repository: BaseGraphRepository) : ViewModel() {
    var seasonSource: SeasonSource? = null

    fun createSource(seasonField: SeasonField): SeasonSource {
        seasonSource = SeasonSource(baseGraphRepository = repository, seasonField = seasonField)
        return seasonSource!!
    }

}
