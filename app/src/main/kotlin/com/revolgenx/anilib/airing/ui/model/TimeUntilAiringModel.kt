package com.revolgenx.anilib.airing.ui.model

import android.content.Context
import com.revolgenx.anilib.R
import timber.log.Timber
import java.util.concurrent.TimeUnit
import anilib.i18n.R as I18nR


class TimeUntilAiringModel(time: Long) {
    var oldTime: Long = 0
    var time: Long = 0
        set(value) {
            field = value
            day = value
            hour = value
            min = value
            sec = value
        }

    val alreadyAired get() = time < 1

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

    fun formatString(context: Context): String {
        return when {
            day != 0L -> {
                context.getString(I18nR.string.eta_s_s_s_s).format(day, hour, min, sec)
            }

            hour != 0L -> {
                context.getString(I18nR.string.eta_s_s_s).format(hour, min, sec)
            }

            min != 0L -> {
                context.getString(I18nR.string.eta_s_s).format(min, sec)
            }

            sec != 0L -> {
                context.getString(I18nR.string.eta_s).format(sec)
            }

            else -> ""
        }
    }

}