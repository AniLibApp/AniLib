package com.revolgenx.anilib.user.fragment


import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.viewmodel.StatsStudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsStudioFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsStudioViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.STUDIO
}
