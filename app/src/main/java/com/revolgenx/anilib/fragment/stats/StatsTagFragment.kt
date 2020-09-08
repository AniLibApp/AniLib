package com.revolgenx.anilib.fragment.stats

import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.viewmodel.stats.StatsTagViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsTagFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsTagViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.TAG
}
