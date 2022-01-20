package com.revolgenx.anilib.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.user.service.UserStatsService
import com.revolgenx.anilib.user.data.field.UserStatisticOverviewField
import com.revolgenx.anilib.user.data.model.UserModel

class StatsOverviewViewModel(private val userStatsService: UserStatsService) : BaseViewModel() {
    var scoreTogglePos: Int = 0
    var releaseYearTogglePos: Int = 0
    var watchYearTogglePos: Int = 0

    var field: UserStatisticOverviewField = UserStatisticOverviewField()

    val statsLiveData = MutableLiveData<Resource<UserModel>>()

    fun getOverview() {
        statsLiveData.value = Resource.loading(null)
        userStatsService.getUserStatisticsOverview(field, compositeDisposable){
            statsLiveData.value = it
        }
    }
}