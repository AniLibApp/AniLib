package com.revolgenx.anilib.viewmodel.media_list

import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.service.media.MediaListEntryService
import com.revolgenx.anilib.source.media_list.MediaListSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

open class MediaListViewModel(
    private val entryService: MediaListEntryService,
    private val service: MediaListService
) : SourceViewModel<MediaListSource, MediaListField>() {
    override var field: MediaListField = MediaListField()

    override fun createSource(): MediaListSource {
        source = MediaListSource(field, service, compositeDisposable)
        return source!!
    }

    fun increaseProgress(
        model: EntryListEditorMediaModel,
        callback: (Resource<EntryListEditorMediaModel>) -> Unit
    ) {
        entryService.increaseProgress(model, compositeDisposable, callback)
    }

}