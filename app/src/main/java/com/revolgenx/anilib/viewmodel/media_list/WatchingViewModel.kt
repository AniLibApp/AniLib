package com.revolgenx.anilib.viewmodel.media_list

import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.media.MediaListEntryService
import com.revolgenx.anilib.viewmodel.media_list.MediaListCollectionViewModel

class WatchingViewModel( mediaListService: MediaListService, entryService: MediaListEntryService) :
    MediaListCollectionViewModel(mediaListService, entryService) {

}
