package com.revolgenx.anilib.viewmodel

import android.content.Context
import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.field.home.PopularMediaField
import com.revolgenx.anilib.preference.getPopularField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MediaSource
import com.revolgenx.anilib.type.MediaSort

class PopularViewModel(private val mediaService: MediaService) :
    SourceViewModel<MediaSource, PopularMediaField>() {


    override var field: PopularMediaField = PopularMediaField().also {
        it.sort = MediaSort.POPULARITY_DESC.ordinal
    }

    override fun createSource(): MediaSource {
        source = MediaSource(mediaService, field, compositeDisposable)
        return source!!
    }

    fun updateField(context: Context) {
        getPopularField(context).let {
            field.season = it.season
            field.seasonYear = it.seasonYear
            field.format = it.format
            field.status = it.status
        }
    }
}
