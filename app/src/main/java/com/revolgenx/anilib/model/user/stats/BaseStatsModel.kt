package com.revolgenx.anilib.model.user.stats

import com.revolgenx.anilib.model.BaseModel
import java.util.concurrent.TimeUnit

abstract class BaseStatsModel : BaseModel() {
    var count: Int? = null
    var meanScore: Double? = null
    var minutesWatched: Int? = null
        set(value) {
            field = value
            hoursWatched = value?.div(60)
            day = value!!.toLong()
            hour = value.toLong()
        }
    var chaptersRead: Int? = null
    var hoursWatched: Int? = null
    var mediaIds:List<Int>? = null


    var day: Long = 0
        set(value) {
            field = TimeUnit.MINUTES.toDays(value)
        }

    var hour: Long = 0
        set(value) {
            field = TimeUnit.MINUTES.toHours(value) - TimeUnit.DAYS.toHours(
                TimeUnit.MINUTES.toDays(value)
            )
        }
}
