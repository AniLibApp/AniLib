package com.revolgenx.anilib.ui.fragment.stats

import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.ui.viewmodel.stats.StatsTagViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsTagFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsTagViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.TAG
}
