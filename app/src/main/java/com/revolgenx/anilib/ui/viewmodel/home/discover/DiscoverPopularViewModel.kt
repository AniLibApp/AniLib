package com.revolgenx.anilib.ui.viewmodel.home.discover

import android.content.Context
import com.revolgenx.anilib.data.field.home.PopularMediaField
import com.revolgenx.anilib.common.preference.getPopularField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class DiscoverPopularViewModel(mediaService: MediaService) :
    BaseDiscoverViewModel<PopularMediaField>(mediaService) {

    override var field: PopularMediaField = PopularMediaField().also {
        it.sort = MediaSort.POPULARITY_DESC.ordinal
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
