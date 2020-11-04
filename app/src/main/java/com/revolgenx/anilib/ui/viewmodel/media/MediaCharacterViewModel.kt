package com.revolgenx.anilib.ui.viewmodel.media

import com.revolgenx.anilib.data.field.media.MediaCharacterField
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.source.MediaCharacterSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

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
