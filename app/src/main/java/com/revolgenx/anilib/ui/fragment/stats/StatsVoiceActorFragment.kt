package com.revolgenx.anilib.ui.fragment.stats

import com.revolgenx.anilib.data.field.stats.UserStatsField
import com.revolgenx.anilib.ui.viewmodel.stats.StatsVoiceActorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsVoiceActorFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsVoiceActorViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.VOICE_ACTOR
}
