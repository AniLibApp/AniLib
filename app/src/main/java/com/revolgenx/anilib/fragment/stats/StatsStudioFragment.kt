package com.revolgenx.anilib.fragment.stats


import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.viewmodel.StatsStudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsStudioFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsStudioViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.STUDIO
}
