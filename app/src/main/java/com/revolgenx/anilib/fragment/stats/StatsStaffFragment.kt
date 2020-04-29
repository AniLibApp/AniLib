package com.revolgenx.anilib.fragment.stats


import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.viewmodel.StatsStaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsStaffFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsStaffViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.STAFF
}
