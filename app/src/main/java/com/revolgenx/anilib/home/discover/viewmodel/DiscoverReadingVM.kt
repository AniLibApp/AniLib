package com.revolgenx.anilib.home.discover.viewmodel

import android.content.Context
import com.revolgenx.anilib.common.preference.getDiscoverMediaListSort
import com.revolgenx.anilib.data.field.list.MediaListField
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.infrastructure.service.list.MediaListService

class DiscoverReadingVM(service: MediaListService,entryService: MediaListEntryService) : MediaListVM(service,entryService) {
    override var field: MediaListField = MediaListField()

    fun updateField() {
        field.sort = getDiscoverMediaListSort(field.type!!)
    }
}