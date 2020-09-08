package com.revolgenx.anilib.fragment.stats

import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.viewmodel.stats.StatsVoiceActorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsVoiceActorFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsVoiceActorViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.VOICE_ACTOR
}
