package com.revolgenx.anilib.viewmodel.media_list

import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.media.MediaListEntryService

class DroppedViewModel(mediaListService: MediaListService, entryService: MediaListEntryService) :
    MediaListCollectionViewModel(mediaListService, entryService) {

}
