package com.revolgenx.anilib.ui.viewmodel.stats

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.user.stats.StatsOverviewField
import com.revolgenx.anilib.data.model.user.stats.StatsOverviewModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.user.UserStatsService
import io.reactivex.disposables.CompositeDisposable

class StatsOverviewViewModel(private val userStatsService: UserStatsService) : ViewModel() {
    var scoreTogglePos: Int = 0
    var releaseYearTogglePos: Int = 0
    var watchYearTogglePos: Int = 0

    var field: StatsOverviewField = StatsOverviewField()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val statsLiveData = MediatorLiveData<Resource<StatsOverviewModel>>().also {
        it.addSource(userStatsService.statsOverviewLiveData) { res ->
            it.value = res
        }
    }

    fun getOverview() {
        statsLiveData.value = Resource.loading(null)
        userStatsService.getStatsOverview(field, compositeDisposable)
    }
}
