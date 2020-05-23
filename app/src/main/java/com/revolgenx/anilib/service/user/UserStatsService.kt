package com.revolgenx.anilib.service.user

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.field.user.stats.StatsOverviewField
import com.revolgenx.anilib.model.user.stats.BaseStatsModel
import com.revolgenx.anilib.model.user.stats.StatsOverviewModel
import com.revolgenx.anilib.repository.util.Resource
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