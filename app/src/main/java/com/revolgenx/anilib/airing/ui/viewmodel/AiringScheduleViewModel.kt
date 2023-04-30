package com.revolgenx.anilib.airing.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.data.source.AiringSchedulePagingSource
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.common.ui.screen.PagingViewModel
import com.revolgenx.anilib.type.AiringSort
import java.time.LocalTime
import java.time.ZonedDateTime

class AiringScheduleViewModel(private val service: AiringScheduleService) :
    PagingViewModel<AiringScheduleModel, AiringScheduleField, AiringSchedulePagingSource>() {
    var startDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MIN))
    var endDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MAX))


    override var field by mutableStateOf(
        AiringScheduleField(
            airingGreaterThan = startDateTime.toEpochSecond().toInt(),
            airingLessThan = endDateTime.toEpochSecond().toInt(),
            sort = AiringSort.TIME_DESC
        )
    )


    override val pagingSource: AiringSchedulePagingSource
        get() = AiringSchedulePagingSource(this.field, service)


    fun previous(){
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

    fun next(){
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

    private fun updateDateTime(){
        field = field.copy(
            airingGreaterThan = startDateTime.toEpochSecond().toInt(),
            airingLessThan = endDateTime.toEpochSecond().toInt(),
        )
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