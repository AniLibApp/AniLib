package com.revolgenx.anilib.airing.data.model

import java.util.concurrent.TimeUnit

class TimeUntilAiringModel {
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
            field = if (value > 0) TimeUnit.SECONDS.toDays(value) else 0
        }

    var hour: Long = 0
        set(value) {
            field = if (value > 0) TimeUnit.SECONDS.toHours(value) - TimeUnit.DAYS.toHours(
                TimeUnit.SECONDS.toDays(value)
            ) else 0
        }
    var min: Long = 0
        set(value) {
            field = if (value > 0) TimeUnit.SECONDS.toMinutes(value) - TimeUnit.HOURS.toMinutes(
                TimeUnit.SECONDS.toHours(value)
            ) else 0
        }
    var sec: Long = 0
        set(value) {
            field =
                if (value > 0) value - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(value)) else 0
        }

    fun getFormattedTime() = "${day}d ${hour}h ${min}m ${sec}s"

}
