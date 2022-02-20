package com.revolgenx.anilib.user.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.user.data.field.UserStatisticOverviewField
import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.data.model.stats.BaseStatisticModel
import com.revolgenx.anilib.user.data.model.stats.StatsOverviewModel
import io.reactivex.disposables.CompositeDisposable

interface UserStatsService {
    val statsOverviewLiveData: MutableLiveData<Resource<StatsOverviewModel>>

    fun getUserStatisticsOverview(
        field: UserStatisticOverviewField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<UserModel>) -> Unit
    )

    fun getUserStats(
        field: UserStatsField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseStatisticModel>>) -> Unit)
    )

}