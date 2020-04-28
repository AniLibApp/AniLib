package com.revolgenx.anilib.model.user.stats

import com.revolgenx.anilib.model.BaseModel

abstract class BaseStatsModel : BaseModel() {
    var count: Int? = null
    var meanScore: Double? = null
    var minutesWatched: Int? = null
        set(value) {
            field = value
            hoursWatched = value?.div(60)
        }

    var hoursWatched: Int? = null
}
