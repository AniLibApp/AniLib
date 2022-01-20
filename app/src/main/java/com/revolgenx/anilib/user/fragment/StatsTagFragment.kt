package com.revolgenx.anilib.user.fragment

import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.viewmodel.StatsTagViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsTagFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsTagViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.TAG
}
