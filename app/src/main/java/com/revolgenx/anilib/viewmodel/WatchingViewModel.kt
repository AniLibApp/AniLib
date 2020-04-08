package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.source.MediaListSource

class WatchingViewModel(mediaListService: MediaListService) : MediaListViewModel(mediaListService) {

    override fun createSource(field: MediaListField): MediaListSource {
        source = MediaListSource(field, list, mediaListService, compositeDisposable)
        return source!!
    }

}
