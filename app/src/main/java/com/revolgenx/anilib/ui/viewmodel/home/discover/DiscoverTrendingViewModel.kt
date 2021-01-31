package com.revolgenx.anilib.ui.viewmodel.home.discover

import android.content.Context
import com.revolgenx.anilib.data.field.home.TrendingMediaField
import com.revolgenx.anilib.common.preference.getTrendingField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.type.MediaSort

class DiscoverTrendingViewModel(mediaService: MediaService) : BaseDiscoverViewModel<TrendingMediaField>(mediaService) {

    override var field: TrendingMediaField = TrendingMediaField().also {
        it.sort = MediaSort.TRENDING_DESC.ordinal
    }

    fun updateField(context: Context) {
        getTrendingField(context).let {
            field.season = it.season
            field.seasonYear = it.seasonYear
            field.format = it.format
            field.status = it.status
            field.formatsIn = it.formatsIn
        }
    }
}
