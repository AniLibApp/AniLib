package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.media.MediaListEntryService

class CompletedViewModel(mediaListService: MediaListService, entryService: MediaListEntryService) :
    MediaListViewModel(mediaListService, entryService) {

}
