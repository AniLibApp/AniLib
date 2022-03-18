package com.revolgenx.anilib.airing.viewmodel

import android.content.Context
import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.common.preference.getAiringField
import com.revolgenx.anilib.common.preference.showAiringWeekly
import com.revolgenx.anilib.airing.service.AiringMediaService
import com.revolgenx.anilib.infrastructure.source.home.airing.AiringSource
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.WeekFields
import java.util.*

class AiringViewModel(private val airingMediaService: AiringMediaService) :
    SourceViewModel<AiringSource, AiringMediaField>() {


    var isDateTypeRange: Boolean = false  //false days true weeks

    var startDateTime = ZonedDateTime.now().with(LocalTime.MIN)
        set(value) {
            field = value
            this.field.airingGreaterThan = field.toEpochSecond().toInt()
        }
    var endDateTime = ZonedDateTime.now().with(LocalTime.MAX)
        set(value) {
            field = value
            this.field.airingLessThan = field.toEpochSecond().toInt()
        }

    private val weekFields = WeekFields.of(Locale.getDefault())

    override var field: AiringMediaField = AiringMediaField()
        .also {
            it.notYetAired = false
            it.airingGreaterThan = startDateTime.toEpochSecond().toInt()
            it.airingLessThan = endDateTime.toEpochSecond().toInt()
            it.sort = AiringSort.TIME_DESC.ordinal
        }

    override fun createSource(): AiringSource {
        source = AiringSource(field, airingMediaService, compositeDisposable)
        return source!!
    }

    fun previous() {
        if (isDateTypeRange) {
            startDateTime = startDateTime.minusWeeks(1)
            endDateTime = endDateTime.minusWeeks(1)
        } else {
            startDateTime = startDateTime.minusDays(1)
            endDateTime = endDateTime.minusDays(1)
        }
        compositeDisposable.clear()
    }

    fun next() {
        if (isDateTypeRange) {

            startDateTime = startDateTime.plusWeeks(1)
            endDateTime = endDateTime.plusWeeks(1)

        } else {
            startDateTime = startDateTime.plusDays(1)
            endDateTime = endDateTime.plusDays(1)
        }
        compositeDisposable.clear()
    }

    fun updateDateRange(rangeType: Boolean) {
        isDateTypeRange = rangeType
        if (isDateTypeRange) {
            startDateTime = ZonedDateTime.now().with(weekFields.dayOfWeek(), 1)
                .with(LocalTime.MIN)
            endDateTime = ZonedDateTime.now().with(weekFields.dayOfWeek(), 7)
                .with(LocalTime.MAX)
        } else {
            startDateTime = ZonedDateTime.now().with(LocalTime.MIN)
            endDateTime = ZonedDateTime.now().with(LocalTime.MAX)
        }
        compositeDisposable.clear()
    }

    fun updateMediaProgress(mediaId: Int?, progress: Int?) {

    }


    fun updateField(context: Context) {
        getAiringField(context).let {
            field.notYetAired = it.notYetAired
            field.sort = it.sort
            field.showFromPlanning = it.showFromPlanning
            field.showFromWatching = it.showFromWatching
            field.isWeeklyTypeDate = showAiringWeekly()
        }
    }

    fun updateWeeklyField(context: Context, bool: Boolean) {
        showAiringWeekly(bool)
        field.isWeeklyTypeDate = bool
    }
}