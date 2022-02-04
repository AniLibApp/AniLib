package com.revolgenx.anilib.home.discover.viewmodel

import android.content.Context
import com.revolgenx.anilib.common.preference.getDiscoverMediaListSort
import com.revolgenx.anilib.data.field.list.MediaListField
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.entry.service.MediaEntryService

class DiscoverWatchingViewModel(entryService: MediaEntryService, service: MediaListService) :
    MediaListViewModel(entryService,service) {
    override var field: MediaListField = MediaListField()

    fun updateField(context: Context) {
        field.sort = getDiscoverMediaListSort(context, field.type!!)
    }
}