package com.revolgenx.anilib.viewmodel.media

import com.revolgenx.anilib.field.media.MediaCharacterField
import com.revolgenx.anilib.service.media.MediaBrowseService
import com.revolgenx.anilib.source.MediaCharacterSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class MediaCharacterViewModel(private val mediaBrowserService: MediaBrowseService) : SourceViewModel<MediaCharacterSource, MediaCharacterField>() {

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
