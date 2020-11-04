package com.revolgenx.anilib.ui.viewmodel.media

import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.MediaSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class MediaViewDialogViewModel(private val service:MediaService) :SourceViewModel<MediaSource, MediaField>(){
    override var field: MediaField = MediaField()

    override fun createSource(): MediaSource {
        source = MediaSource(service,field,  compositeDisposable)
        return source!!
    }
}
