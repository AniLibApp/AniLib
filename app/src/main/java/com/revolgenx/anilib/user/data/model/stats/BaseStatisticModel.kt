package com.revolgenx.anilib.user.data.model.stats

import com.revolgenx.anilib.common.data.model.BaseModel
import java.util.concurrent.TimeUnit

abstract class BaseStatisticModel : BaseModel() {
    var count: Int = 0
    var meanScore: Double = 0.0
    var minutesWatched: Int = 0
        set(value) {
            field = value
            hoursWatched = value.div(60)
            day = value.toLong()
            hour = value.toLong()
        }

    var chaptersRead: Int? = null
    var mediaIds:List<Int>? = null

    //conversion
    var hoursWatched: Int = 0
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
