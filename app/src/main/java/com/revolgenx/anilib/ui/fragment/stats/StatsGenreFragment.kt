package com.revolgenx.anilib.ui.fragment.stats

import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.ui.viewmodel.stats.StatsGenreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//TODO SHOW ANIME
class StatsGenreFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsGenreViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.GENRE
}
