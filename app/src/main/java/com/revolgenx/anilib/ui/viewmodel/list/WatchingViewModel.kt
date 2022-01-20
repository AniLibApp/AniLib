package com.revolgenx.anilib.ui.viewmodel.list

import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.entry.service.MediaEntryService
import com.revolgenx.anilib.type.MediaListStatus

class WatchingViewModel(mediaListService: MediaListService, entryService: MediaEntryService) :
    MediaListCollectionViewModel(mediaListService, entryService) {

    override var field: MediaListCollectionField = MediaListCollectionField().also {
        it.mediaListStatus = MediaListStatus.CURRENT.ordinal
    }
}
