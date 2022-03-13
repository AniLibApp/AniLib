package com.revolgenx.anilib.home.discover.viewmodel

import android.content.Context
import com.revolgenx.anilib.common.preference.getPopularField
import com.revolgenx.anilib.home.discover.data.field.PopularMediaField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.type.MediaSort

class DiscoverPopularViewModel(mediaService: MediaService) :
    BaseDiscoverViewModel<PopularMediaField>(mediaService) {

    override var field: PopularMediaField = PopularMediaField().also {
        it.sort = MediaSort.POPULARITY_DESC.ordinal
    }

    fun updateField() {
        getPopularField().let {
            field.season = it.season
            field.year = it.year
            field.format = it.format
            field.status = it.status
            field.formatsIn = it.formatsIn
        }
    }
}