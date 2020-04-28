package com.revolgenx.anilib.fragment.stats

import com.revolgenx.anilib.model.user.stats.StatsGenreModel
import com.revolgenx.anilib.viewmodel.StatsGenreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsGenreFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsGenreViewModel>()

}
