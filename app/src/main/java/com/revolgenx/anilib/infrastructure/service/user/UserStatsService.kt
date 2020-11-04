package com.revolgenx.anilib.infrastructure.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.data.field.user.stats.StatsOverviewField
import com.revolgenx.anilib.data.model.user.stats.BaseStatsModel
import com.revolgenx.anilib.data.model.user.stats.StatsOverviewModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface UserStatsService {
    val statsOverviewLiveData: MutableLiveData<Resource<StatsOverviewModel>>

    fun getStatsOverview(
        field: StatsOverviewField,
        compositeDisposable: CompositeDisposable
    )

    fun getUserStats(
        field: UserStatsField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseStatsModel>>) -> Unit)
    )

}