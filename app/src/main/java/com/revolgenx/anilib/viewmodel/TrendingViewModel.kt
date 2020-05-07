package com.revolgenx.anilib.viewmodel

import android.content.Context
import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.field.home.TrendingMediaField
import com.revolgenx.anilib.preference.getTrendingField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MediaSource
import com.revolgenx.anilib.type.MediaSort

class TrendingViewModel(private val mediaService: MediaService) :
    SourceViewModel<MediaSource, TrendingMediaField>() {

    var adapter: Adapter? = null

    override var field: TrendingMediaField = TrendingMediaField().also {
        it.sort = MediaSort.TRENDING_DESC.ordinal
    }

    override fun createSource(): MediaSource {
        source = MediaSource(mediaService, field, compositeDisposable)
        return source!!
    }

    fun updateField(context: Context) {
        getTrendingField(context).let {
            field.season = it.season
            field.seasonYear = it.seasonYear
            field.format = it.format
            field.status = it.status
        }
    }
}
