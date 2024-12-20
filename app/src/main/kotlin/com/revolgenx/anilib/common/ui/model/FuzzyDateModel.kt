package com.revolgenx.anilib.common.ui.model

import com.revolgenx.anilib.fragment.FuzzyDate
    import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale


data class FuzzyDateModel(val day: Int? = null, val month: Int? = null, val year: Int? = null) {
    val shortDate
        get() = "${
            month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
        } ${day?.let { "$it," } ?: ""} ${year ?: ""}"

    override fun toString(): String {
        return "${year ?: ""}${month?.let { "-$it" } ?: ""}${day?.let { "-$it" } ?: ""}"
    }

    fun isEmpty() = year == null && month == null && day == null

    fun toZoneDateTime(): ZonedDateTime? {
        val now = LocalDate.now()
        return LocalDate.of(
            year ?: now.year,
            month ?: now.monthValue,
            day ?: now.dayOfMonth
        ).atStartOfDay(ZoneOffset.UTC)
    }

    companion object {
        fun from(zonedDateTime: ZonedDateTime): FuzzyDateModel {
            return FuzzyDateModel(
                zonedDateTime.dayOfMonth,
                zonedDateTime.monthValue,
                zonedDateTime.year
            )
        }
    }

    fun toFuzzyDateInt() = (year ?: 0) * 10000 + (month ?: 0) * 100 + (day ?: 0)
}

fun FuzzyDate.toModel() = FuzzyDateModel(
    day,
    month,
    year
).takeIf { it.year != null || it.month != null || it.day != null }