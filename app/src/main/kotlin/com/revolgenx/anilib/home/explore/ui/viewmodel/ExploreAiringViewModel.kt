package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

class ExploreAiringViewModel : ViewModel() {
    val weekDaysFromToday = mutableStateOf(mapOf<String, Long>())
    var selectedDay = mutableStateOf("")

    init {
        refreshWeekDaysFromToday()
    }

    fun refreshWeekDaysFromToday() {
        val todayDate = LocalDate.now()
        val weekDays = mutableMapOf<String, Long>()
        for (i in 0 until 7) {
            val newDate = todayDate.plusDays(i.toLong())
            val dayOfWeek = newDate.dayOfWeek
            val currentLocale =
                AppCompatDelegate.getApplicationLocales().get(0) ?: Locale.getDefault()
            val newDayString = dayOfWeek.getDisplayName(TextStyle.SHORT, currentLocale)
            if (i == 0) {
                selectedDay.value = newDayString
            }
            weekDays[newDayString] =
                newDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000L
        }
        weekDaysFromToday.value = weekDays
    }
}