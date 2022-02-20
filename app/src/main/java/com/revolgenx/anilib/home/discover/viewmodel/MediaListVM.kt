package com.revolgenx.anilib.home.discover.viewmodel

import com.revolgenx.anilib.data.field.list.MediaListField
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.entry.service.increaseProgress
import com.revolgenx.anilib.list.data.model.MediaListModel

open class MediaListVM(
    private val service: MediaListService,
    private val entryService: MediaListEntryService
) : SourceViewModel<MediaListSource, MediaListField>() {
    override var field: MediaListField = MediaListField()

    override fun createSource(): MediaListSource {
        source = MediaListSource(field, service, compositeDisposable)
        return source!!
    }

    fun increaseProgress(
        item: MediaListModel
    ) {
        entryService.increaseProgress(item, compositeDisposable)
    }

}