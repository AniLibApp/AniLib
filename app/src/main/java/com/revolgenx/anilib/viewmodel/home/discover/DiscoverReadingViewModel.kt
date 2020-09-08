package com.revolgenx.anilib.viewmodel.home.discover

import android.content.Context
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.preference.getDiscoverMediaListSort
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.media.MediaListEntryService
import com.revolgenx.anilib.viewmodel.media_list.MediaListViewModel

class DiscoverReadingViewModel(entryService: MediaListEntryService,service: MediaListService) :
    MediaListViewModel(entryService,service) {
    override var field: MediaListField = MediaListField()

    fun updateField(context: Context) {
        field.sort = getDiscoverMediaListSort(context, field.type!!)
    }
}