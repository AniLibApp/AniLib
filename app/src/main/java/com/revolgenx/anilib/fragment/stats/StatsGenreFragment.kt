package com.revolgenx.anilib.fragment.stats

import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.viewmodel.StatsGenreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//TODO SHOW ANIME
class StatsGenreFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsGenreViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.GENRE
}
