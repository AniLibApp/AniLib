package com.revolgenx.anilib.home.discover.viewmodel

import android.content.Context
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.home.discover.data.field.NewlyAddedMediaField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.type.MediaSort

class DiscoverNewViewModel(mediaService: MediaService) :
    BaseDiscoverViewModel<NewlyAddedMediaField>(mediaService) {

    override var field: NewlyAddedMediaField = NewlyAddedMediaField().also {
        it.sort = MediaSort.ID_DESC.ordinal
    }


    fun updateField(context: Context) {
        getNewlyAddedField(context).let {
            field.format = it.format
            field.year = it.year
            field.season = it.season
            field.status = it.status
            field.formatsIn = it.formatsIn
        }
    }
}