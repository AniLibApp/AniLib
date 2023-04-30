package com.revolgenx.anilib.airing.ui.model

import timber.log.Timber
import java.util.concurrent.TimeUnit


class TimeUntilAiringModel(time: Long = 0) {
    var oldTime: Long = 0
    var time: Long = 0
        set(value) {
            field = value
            day = value
            hour = value
            min = value
            sec = value
        }

    var day: Long = 0
        private set(value) {
            field = if (value > 0) TimeUnit.SECONDS.toDays(value) else 0
        }

    var hour: Long = 0
        private set(value) {
            field = if (value > 0) TimeUnit.SECONDS.toHours(value) - TimeUnit.DAYS.toHours(
                TimeUnit.SECONDS.toDays(value)
            ) else 0
        }
    var min: Long = 0
        private set(value) {
            field = if (value > 0) TimeUnit.SECONDS.toMinutes(value) - TimeUnit.HOURS.toMinutes(
                TimeUnit.SECONDS.toHours(value)
            ) else 0
        }
    var sec: Long = 0
        private set(value) {
            field =
                if (value > 0) value - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(value)) else 0
        }


    init {
        this.time = time
        this.oldTime = time
    }

    fun getFormattedTime() = "${day}d ${hour}h ${min}m ${sec}s"

    override fun hashCode(): Int {
        return time.hashCode()
    }
}