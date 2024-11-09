package com.revolgenx.anilib.airing.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.data.source.AiringSchedulePagingSource
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.WeekFields
import java.util.Locale

/*TODO calendar locale start of the week  for week range*/
open class AiringScheduleViewModel(
    private val service: AiringScheduleService,
    private val airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    private val authPreferencesDataStore: AppPreferencesDataStore
) :
    PagingViewModel<BaseModel, AiringScheduleField, AiringSchedulePagingSource>() {

    private var filter = airingScheduleFilterDataStore.data.get()
    private val weekFields = WeekFields.of(Locale.getDefault())
    var startDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MIN))
    var endDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MAX))

    override var field by mutableStateOf(
        filter.toField().also { field ->
            field.userId = authPreferencesDataStore.userId.get()
            field.needMediaListData = field.showOnlyWatching || field.showOnlyPlanning
            updateWeeklyDateTimeRange(field.isWeeklyTypeDate)
            updateFieldDateTime(field)
        }
    )

    init {
        launch {
            airingScheduleFilterDataStore.data.collect { newFilter ->
                if (newFilter == filter) return@collect
                filter = newFilter
                field = newFilter.toField().also { newField ->
                    newField.userId = authPreferencesDataStore.userId.get()
                    newField.mediaListIds = field.mediaListIds
                    updateFieldDateTime(newField)
                    updateFieldNeedMediaListData(newField)
                }
                refresh()
            }
        }
    }

    override val pagingSource: AiringSchedulePagingSource
        get() = AiringSchedulePagingSource(this.field, service)


    fun previous() {
        if (field.isWeeklyTypeDate) {
            startDateTime = startDateTime.minusWeeks(1)
            endDateTime = endDateTime.minusWeeks(1)
        } else {
            startDateTime = startDateTime.minusDays(1)
            endDateTime = endDateTime.minusDays(1)
        }
        updateFieldDateTime()
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
        updateFieldDateTime()
        refresh()
    }

    fun updateStartDate(startDate: Long) {
        startDateTime =
            Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).with(LocalTime.MIN)
        endDateTime =
            Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).with(LocalTime.MAX)
        updateFieldDateTime()
        refresh()
    }

    fun updateDates(startDate: Long, endDate: Long) {
        startDateTime =
            Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).with(LocalTime.MIN)
        endDateTime =
            Instant.ofEpochMilli(endDate).atZone(ZoneOffset.UTC).with(LocalTime.MAX)
        updateFieldDateTime()
        refresh()
    }

    private fun updateFieldDateTime(mField: AiringScheduleField? = null) {
        val field = mField ?: field
        field.airingGreaterThan = startDateTime.toEpochSecond().toInt()
        field.airingLessThan = endDateTime.toEpochSecond().toInt()
    }

    private fun updateWeeklyDateTimeRange(isWeeklyTypeDate: Boolean) {
        if (isWeeklyTypeDate) {
            startDateTime = ZonedDateTime.now().with(weekFields.dayOfWeek(), 1)
                .with(LocalTime.MIN)
            endDateTime = ZonedDateTime.now().with(weekFields.dayOfWeek(), 7)
                .with(LocalTime.MAX)
        } else {
            startDateTime = ZonedDateTime.now().with(LocalTime.MIN)
            endDateTime = ZonedDateTime.now().with(LocalTime.MAX)
        }
    }

    fun updateDateRange(isWeeklyTypeDate: Boolean) {
        updateWeeklyDateTimeRange(isWeeklyTypeDate)

        launch {
            field.isWeeklyTypeDate = isWeeklyTypeDate
            airingScheduleFilterDataStore.updateData {
                field.toFilterData()
            }
        }
    }

    private fun updateFieldNeedMediaListData(mField: AiringScheduleField) {
        var needMediaListData = false
        if ((mField.showOnlyPlanning || mField.showOnlyWatching)
            && (mField.showOnlyWatching != field.showOnlyWatching
                    || mField.showOnlyPlanning != field.showOnlyPlanning)
        ) {
            needMediaListData = true
        }
        mField.needMediaListData = needMediaListData
    }
}