package com.revolgenx.anilib.home.explore.ui.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleFilterViewModel
import com.revolgenx.anilib.airing.ui.viewmodel.AiringScheduleViewModel
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.type.AiringSort
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

class ExploreAiringScheduleViewModel(
    service: AiringScheduleService,
    airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    appPreferencesDataStore: AppPreferencesDataStore
) : AiringScheduleViewModel(service, airingScheduleFilterDataStore, appPreferencesDataStore){

    val weekDaysFromToday = mutableStateOf(mapOf<String, Long>())
    var selectedDay = mutableStateOf("")
    val isFiltered get() = field.run { showOnlyPlanning || showOnlyWatching || isWeeklyTypeDate || sort != AiringSort.TIME || !notYetAired }

    init {
        initializeWeekDays(LocalDate.now())
    }

    fun refreshWeekDays(){
        val now = ZonedDateTime.now()
        initializeWeekDays(now.toLocalDate())
        updateStartDate(now.toEpochSecond())
        refresh()
    }

    private fun initializeWeekDays(todayDate: LocalDate) {
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

class ExploreAiringScheduleFilterViewModel(
    airingScheduleFilterDataStore: AiringScheduleFilterDataStore
) : AiringScheduleFilterViewModel(airingScheduleFilterDataStore)