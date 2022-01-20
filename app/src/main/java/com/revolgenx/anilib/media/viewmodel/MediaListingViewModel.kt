package com.revolgenx.anilib.media.viewmodel

import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaAdapterSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class MediaListingViewModel(private val service: MediaService) :
    SourceViewModel<MediaAdapterSource, MediaField>() {
    override var field: MediaField = MediaField()

    override fun createSource(): MediaAdapterSource {
        source = MediaAdapterSource(service, field, compositeDisposable)
        return source!!
    }
}
