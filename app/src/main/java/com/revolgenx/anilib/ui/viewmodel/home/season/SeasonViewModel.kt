package com.revolgenx.anilib.ui.viewmodel.home.season

import android.content.Context
import com.revolgenx.anilib.data.field.home.SeasonField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class SeasonViewModel(private val service: MediaService) :
    SourceViewModel<MediaSource, SeasonField>() {

    override var field: SeasonField = SeasonField()

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
        dispose()
        field.nextSeason(context)
    }

    fun previousSeason(context: Context) {
        dispose()
        field.previousSeason(context)
    }

    fun isHeaderEnabled(checked:Boolean){
        compositeDisposable.clear()
        field.showFormatHeader = checked
    }

    fun isHeaderEnabled(): Boolean {
        return field.showFormatHeader
    }

    fun dispose(){
        compositeDisposable.clear()
    }

}
