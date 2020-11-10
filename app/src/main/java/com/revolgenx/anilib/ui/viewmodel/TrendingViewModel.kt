package com.revolgenx.anilib.ui.viewmodel

import android.content.Context
import com.revolgenx.anilib.data.field.home.TrendingMediaField
import com.revolgenx.anilib.common.preference.getTrendingField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaSource
import com.revolgenx.anilib.type.MediaSort

class TrendingViewModel(private val mediaService: MediaService) :
    SourceViewModel<MediaSource, TrendingMediaField>() {

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