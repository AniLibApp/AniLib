package com.revolgenx.anilib.user.fragment

import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.viewmodel.StatsGenreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//TODO SHOW ANIME
abstract class UserStatsGenreFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsGenreViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.GENRE
}
