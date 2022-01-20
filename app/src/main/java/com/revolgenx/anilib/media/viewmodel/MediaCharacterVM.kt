package com.revolgenx.anilib.media.viewmodel

import com.revolgenx.anilib.media.data.field.MediaCharacterField
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.infrastructure.source.MediaCharacterSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class MediaCharacterVM(private val mediaBrowserService: MediaInfoService) : SourceViewModel<MediaCharacterSource, MediaCharacterField>() {

    override var field: MediaCharacterField = MediaCharacterField()

    override fun createSource(): MediaCharacterSource {
        source = MediaCharacterSource(
            field = field,
            mediaBrowseService = mediaBrowserService,
            compositeDisposable = compositeDisposable
        )
        return source!!
    }
}
