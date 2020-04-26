package com.revolgenx.anilib.model.user.stats

abstract class BaseStatsModel {
    var count: Int? = null
    var meanScore: Double? = null
    var hourWatched: Int? = null
        set(value) {
            field = value?.div(60)
        }
}
