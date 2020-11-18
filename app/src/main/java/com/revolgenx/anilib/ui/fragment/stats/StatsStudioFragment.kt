package com.revolgenx.anilib.ui.fragment.stats


import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.ui.viewmodel.stats.StatsStudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsStudioFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsStudioViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.STUDIO
}
