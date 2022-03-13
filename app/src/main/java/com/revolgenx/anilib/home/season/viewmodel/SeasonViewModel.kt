package com.revolgenx.anilib.home.season.viewmodel

import android.content.Context
import com.revolgenx.anilib.home.season.data.field.SeasonField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaAdapterSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class SeasonViewModel(private val service: MediaService) :
    SourceViewModel<MediaAdapterSource, SeasonField>() {

    override var field: SeasonField = SeasonField()

    override fun createSource(): MediaAdapterSource {
        source = MediaAdapterSource(
            service,
            field,
            compositeDisposable
        )
        return source!!
    }

    fun updateMediaProgress(mediaId: Int?, progress: Int?) {
        source?.resources?.get(mediaId)?.mediaListEntry?.progress = progress
    }

    fun nextSeason() {
        dispose()
        field.nextSeason()
    }

    fun previousSeason(context: Context) {
        dispose()
        field.previousSeason()
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