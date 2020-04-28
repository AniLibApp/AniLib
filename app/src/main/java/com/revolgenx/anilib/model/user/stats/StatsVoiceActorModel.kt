package com.revolgenx.anilib.model.user.stats

import com.revolgenx.anilib.model.VoiceActorModel

class StatsVoiceActorModel : BaseStatsModel() {
    var voiceActorId: Int? = null
        set(value) {
            field = value
            baseId = value
        }
    var name: String? = null
}