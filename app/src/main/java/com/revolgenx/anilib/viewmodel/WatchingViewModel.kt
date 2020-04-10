package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.service.MediaListEntryService
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.source.MediaListSource

class WatchingViewModel(mediaListService: MediaListService, entryService: MediaListEntryService) :
    MediaListViewModel(mediaListService, entryService) {

}
