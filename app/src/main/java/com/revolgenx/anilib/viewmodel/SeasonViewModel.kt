package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MedianSource

class SeasonViewModel(private val service: MediaService) :
    SourceViewModel<MedianSource, SeasonField>() {
    private val field = SeasonField()

    override fun createSource(field: SeasonField): MedianSource {
        source = MedianSource(
            service,
            field,
            compositeDisposable
        )
        return source!!
    }

    fun updateMediaProgress(mediaId: Int?, progress:Int?) {
        source?.resources?.get(mediaId)?.mediaEntryListModel?.progress = progress
    }

}
