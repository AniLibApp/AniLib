package com.revolgenx.anilib.viewmodel

import android.content.Context
import com.revolgenx.anilib.field.home.SeasonField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MediaSource

class SeasonViewModel(private val service: MediaService) :
    SourceViewModel<MediaSource, SeasonField>() {

    override var field: SeasonField =
        SeasonField()

    override fun createSource(): MediaSource {
        source = MediaSource(
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
    }

    fun previousSeason(context: Context) {
        compositeDisposable.clear()
        field.previousSeason(context)
    }

}
