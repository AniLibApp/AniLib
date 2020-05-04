package com.revolgenx.anilib.model

import java.util.concurrent.TimeUnit

class AiringTime {
    var time: Long = 0
        set(value) {
            field = value
            day = value
            hour = value
            min = value
            sec = value
        }

    var day: Long = 0
        set(value) {
            field = TimeUnit.SECONDS.toDays(value)
        }

    var hour: Long = 0
        set(value) {
            field = TimeUnit.SECONDS.toHours(value) - TimeUnit.DAYS.toHours(
                TimeUnit.SECONDS.toDays(value)
            )
        }
    var min: Long = 0
        set(value) {
            field = TimeUnit.SECONDS.toMinutes(value) - TimeUnit.HOURS.toMinutes(
                TimeUnit.SECONDS.toHours(value)
            )
        }
    var sec: Long = 0
        set(value) {
            field = value - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(value))
        }

    fun getFormattedTime() = "${day}d ${hour}h ${min}m ${sec}s"

}
