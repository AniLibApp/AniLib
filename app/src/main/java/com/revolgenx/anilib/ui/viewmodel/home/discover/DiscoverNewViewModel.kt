package com.revolgenx.anilib.ui.viewmodel.home.discover

import android.content.Context
import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.data.field.home.NewlyAddedMediaField
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class DiscoverNewViewModel(mediaService: MediaService) :
    BaseDiscoverViewModel<NewlyAddedMediaField>(mediaService) {

    override var field: NewlyAddedMediaField = NewlyAddedMediaField().also {
        it.sort = MediaSort.ID_DESC.ordinal
    }


    fun updateField(context: Context) {
        getNewlyAddedField(context).let {
            field.format = it.format
            field.seasonYear = it.seasonYear
            field.season = it.season
            field.status = it.status
        }
    }
}
