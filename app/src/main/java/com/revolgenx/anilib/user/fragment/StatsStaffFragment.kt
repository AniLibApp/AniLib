package com.revolgenx.anilib.user.fragment


import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.viewmodel.StatsStaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsStaffFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsStaffViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.STAFF
}
