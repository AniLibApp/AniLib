package com.revolgenx.anilib.viewmodel.media

import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.service.media.MediaService
import com.revolgenx.anilib.source.MediaSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class MediaViewDialogViewModel(private val service:MediaService) :SourceViewModel<MediaSource, MediaField>(){
    override var field: MediaField = MediaField()

    override fun createSource(): MediaSource {
        source = MediaSource(service,field,  compositeDisposable)
        return source!!
    }
}
