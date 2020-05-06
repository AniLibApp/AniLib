package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.home.airing.AiringMediaField
import com.revolgenx.anilib.service.airing.AiringMediaService
import com.revolgenx.anilib.source.home.airing.AiringSource
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import timber.log.Timber

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

    override var field: AiringMediaField =  AiringMediaField().also {
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
