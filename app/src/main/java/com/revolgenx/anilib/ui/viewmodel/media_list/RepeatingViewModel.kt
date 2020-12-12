package com.revolgenx.anilib.ui.viewmodel.media_list

import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.infrastructure.service.media.MediaListEntryService
import com.revolgenx.anilib.type.MediaListStatus

class RepeatingViewModel(mediaListService: MediaListService, entryService: MediaListEntryService) :
    MediaListCollectionViewModel(mediaListService, entryService) {

    override var field: MediaListCollectionField = MediaListCollectionField().also {
        it.mediaListStatus = MediaListStatus.REPEATING.ordinal
    }
}
