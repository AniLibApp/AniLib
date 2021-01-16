package com.revolgenx.anilib.ui.viewmodel.home.discover

import android.content.Context
import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.common.preference.getAiringField
import com.revolgenx.anilib.common.preference.getTrendingField
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.infrastructure.service.airing.AiringMediaService
import com.revolgenx.anilib.infrastructure.source.home.airing.AiringSource
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class DiscoverAiringViewModel(private val airingMediaService: AiringMediaService) :
    SourceViewModel<AiringSource, AiringMediaField>() {


    private val startDateTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MIN)
    private val endDateTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MAX)
    var selectedDay = 0


    override var field: AiringMediaField = AiringMediaField()
        .also {
            it.sort = AiringSort.TIME.ordinal
            it.airingGreaterThan = startDateTime.toEpochSecond().toInt()
            it.airingLessThan = endDateTime.toEpochSecond().toInt()
        }

    var adapter: Adapter? = null

    override fun createSource(): AiringSource {
        source = AiringSource(field, airingMediaService, compositeDisposable)
        return source!!
    }

    fun onNewDaySelected(day: Int) {
        selectedDay = day
        field.airingGreaterThan = startDateTime.plusDays(day.toLong()).toEpochSecond().toInt()
        field.airingLessThan = endDateTime.plusDays(day.toLong()).toEpochSecond().toInt()
        compositeDisposable.clear()
    }


    fun updateField(context: Context) {
        getAiringField(context).let {
            field.notYetAired = it.notYetAired
            field.sort = it.sort
            field.showFromPlanning = it.showFromPlanning
            field.showFromWatching = it.showFromWatching
        }
    }


    override fun onCleared() {
        adapter?.releasePages()
        super.onCleared()
    }

}
