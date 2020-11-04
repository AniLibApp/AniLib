package com.revolgenx.anilib.ui.fragment.stats


import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.ui.viewmodel.stats.StatsStaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsStaffFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsStaffViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.STAFF
}
