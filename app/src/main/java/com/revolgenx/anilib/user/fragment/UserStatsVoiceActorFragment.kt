package com.revolgenx.anilib.user.fragment

import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.user.viewmodel.StatsVoiceActorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserStatsVoiceActorFragment : UserStatsFragment() {
    override val viewModel by viewModel<StatsVoiceActorViewModel>()
    override val statsType: UserStatsField.UserStatsType = UserStatsField.UserStatsType.VOICE_ACTOR
}
