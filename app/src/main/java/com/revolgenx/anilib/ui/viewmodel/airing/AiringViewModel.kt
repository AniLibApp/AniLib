package com.revolgenx.anilib.ui.viewmodel.airing

import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.infrastructure.service.airing.AiringMediaService
import com.revolgenx.anilib.infrastructure.source.home.airing.AiringSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

//todo update progress after bookmark
class AiringViewModel(private val airingMediaService: AiringMediaService) :
    SourceViewModel<AiringSource, AiringMediaField>() {

    var startDateTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MIN)
        set(value) {
            field = value
            this.field.airingGreaterThan = field.toEpochSecond().toInt()
        }
    var endDateTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MAX)
        set(value) {
            field = value
            this.field.airingLessThan = field.toEpochSecond().toInt()
        }

    override var field: AiringMediaField =  AiringMediaField()
        .also {
        it.notYetAired = false
        it.airingGreaterThan = startDateTime.toEpochSecond().toInt()
        it.airingLessThan = endDateTime.toEpochSecond().toInt()
    }

    override fun createSource(): AiringSource {
        source = AiringSource(field, airingMediaService, compositeDisposable)
        return source!!
    }

    fun previous() {
        startDateTime = startDateTime.minusDays(1)
        endDateTime = endDateTime.minusDays(1)
        compositeDisposable.clear()
    }

    fun next() {
        startDateTime = startDateTime.plusDays(1)
        endDateTime = endDateTime.plusDays(1)
        compositeDisposable.clear()
    }

    fun updateDate(zonedDateTime: ZonedDateTime){
        startDateTime = zonedDateTime.with(LocalTime.MIN)
        endDateTime = zonedDateTime.with(LocalTime.MAX)
        compositeDisposable.clear()
    }

    fun updateMediaProgress(mediaId: Int?, progress: Int?){

    }
}
