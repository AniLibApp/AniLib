package com.revolgenx.anilib.airing.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.data.source.AiringScheduleDailyPagingSource
import com.revolgenx.anilib.airing.data.source.AiringSchedulePagingSource
import com.revolgenx.anilib.airing.data.source.AiringScheduleWeeklyPagingSource
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.BaseAiringScheduleModel
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.type.AiringSort
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.WeekFields
import java.util.Locale

class AiringScheduleViewModel(private val service: AiringScheduleService) :
    PagingViewModel<BaseAiringScheduleModel, AiringScheduleField, AiringSchedulePagingSource<BaseAiringScheduleModel, AiringScheduleField>>() {
    var startDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MIN))
    var endDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MAX))

    private val weekFields = WeekFields.of(Locale.getDefault())

    override var field by mutableStateOf(
        AiringScheduleField(
            airingGreaterThan = startDateTime.toEpochSecond().toInt(),
            airingLessThan = endDateTime.toEpochSecond().toInt(),
            sort = AiringSort.TIME
        )
    )


    override val pagingSource: AiringSchedulePagingSource<BaseAiringScheduleModel, AiringScheduleField>
        get() = if (this.field.isWeeklyTypeDate) {
            AiringScheduleWeeklyPagingSource(this.field, service)
        } else {
            AiringScheduleDailyPagingSource(this.field, service)
        }


    fun previous() {
        if (field.isWeeklyTypeDate) {
            startDateTime = startDateTime.minusWeeks(1)
            endDateTime = endDateTime.minusWeeks(1)
        } else {
            startDateTime = startDateTime.minusDays(1)
            endDateTime = endDateTime.minusDays(1)
        }
        updateDateTime()
        refresh()
    }

    fun next() {
        if (field.isWeeklyTypeDate) {
            startDateTime = startDateTime.plusWeeks(1)
            endDateTime = endDateTime.plusWeeks(1)
        } else {
            startDateTime = startDateTime.plusDays(1)
            endDateTime = endDateTime.plusDays(1)
        }
        updateDateTime()
        refresh()
    }

    private fun updateDateTime() {
        field = field.copy(
            airingGreaterThan = startDateTime.toEpochSecond().toInt(),
            airingLessThan = endDateTime.toEpochSecond().toInt(),
        )
    }

    fun updateDateRange(isWeeklyTypeDate: Boolean) {
        if (isWeeklyTypeDate) {
            startDateTime = ZonedDateTime.now().with(weekFields.dayOfWeek(), 1)
                .with(LocalTime.MIN)
            endDateTime = ZonedDateTime.now().with(weekFields.dayOfWeek(), 7)
                .with(LocalTime.MAX)
        } else {
            startDateTime = ZonedDateTime.now().with(LocalTime.MIN)
            endDateTime = ZonedDateTime.now().with(LocalTime.MAX)
        }
        field = field.copy(
            isWeeklyTypeDate = isWeeklyTypeDate,
            airingGreaterThan = startDateTime.toEpochSecond().toInt(),
            airingLessThan = endDateTime.toEpochSecond().toInt(),
        )
        refresh()
    }

    fun updateField(mField: AiringScheduleField) {
        var needMediaListData = false
        if ((mField.showOnlyPlanning || mField.showOnlyWatching)
            && (mField.showOnlyWatching != field.showOnlyWatching
                    || mField.showOnlyPlanning != field.showOnlyPlanning)
        ) {
            needMediaListData = true
        }
        field = mField.copy(
            needMediaListData = needMediaListData
        )
        refresh()
    }
}