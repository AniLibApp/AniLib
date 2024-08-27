package com.revolgenx.anilib.airing.ui.model

import android.content.Context
import java.time.Instant
import java.util.concurrent.TimeUnit
import anilib.i18n.R as I18nR


data class TimeUntilAiringModel(
    val airingAt: Long,
    var timeUntilAired: Long,
    var day: Long = 0,
    var hour: Long = 0,
    var min: Long = 0,
    var sec: Long = 0
) {
    val alreadyAired get() = timeUntilAired < 1

    init {
        updateDateTime()
    }

    private fun updateDateTime() {
        day = if (timeUntilAired > 0) TimeUnit.SECONDS.toDays(timeUntilAired) else 0
        hour =
            if (timeUntilAired > 0) TimeUnit.SECONDS.toHours(timeUntilAired) - TimeUnit.DAYS.toHours(
                TimeUnit.SECONDS.toDays(timeUntilAired)
            ) else 0
        min =
            if (timeUntilAired > 0) TimeUnit.SECONDS.toMinutes(timeUntilAired) - TimeUnit.HOURS.toMinutes(
                TimeUnit.SECONDS.toHours(timeUntilAired)
            ) else 0
        sec = if (timeUntilAired > 0) timeUntilAired - TimeUnit.MINUTES.toSeconds(
            TimeUnit.SECONDS.toMinutes(timeUntilAired)
        ) else 0
    }

    fun tick() {
        if (alreadyAired) return
        timeUntilAired -= 1
        updateDateTime()
    }

    fun renew() {
        if (alreadyAired) return
        timeUntilAired = airingAt.minus(Instant.now().epochSecond)
        updateDateTime()
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