package com.revolgenx.anilib.viewmodel

import android.content.Context
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MedianSource

class SeasonViewModel(private val service: MediaService) :
    SourceViewModel<MedianSource, SeasonField>() {

    override var field: SeasonField = SeasonField()

    override fun createSource(): MedianSource {
        source = MedianSource(
            service,
            field,
            compositeDisposable
        )
        return source!!
    }

    fun updateMediaProgress(mediaId: Int?, progress: Int?) {
        source?.resources?.get(mediaId)?.mediaEntryListModel?.progress = progress
    }

    fun nextSeason(context: Context) {
        compositeDisposable.clear()
        field.nextSeason(context)
        createSource()
    }

    fun previousSeason(context: Context) {
        compositeDisposable.clear()
        field.previousSeason(context)
        createSource()
    }

}
