package com.revolgenx.anilib.viewmodel

import android.content.Context
import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.field.home.NewlyAddedMediaField
import com.revolgenx.anilib.preference.getNewlyAddedField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MediaSource
import com.revolgenx.anilib.type.MediaSort

class DiscoverNewViewModel(private val mediaService: MediaService) :
    SourceViewModel<MediaSource, NewlyAddedMediaField>() {

    var adapter: Adapter? = null

    override var field: NewlyAddedMediaField = NewlyAddedMediaField().also {
        it.sort = MediaSort.ID_DESC.ordinal
    }

    override fun createSource(): MediaSource {
        source = MediaSource(mediaService, field, compositeDisposable)
        return source!!
    }

    fun updateField(context: Context) {
        getNewlyAddedField(context).let {
            field.format = it.format
        }
    }
}
