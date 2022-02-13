package com.revolgenx.anilib.home.discover.viewmodel

import com.revolgenx.anilib.data.field.list.MediaListField
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel
import com.revolgenx.anilib.list.data.model.MediaListModel

open class MediaListViewModel(
    private val service: MediaListService
) : SourceViewModel<MediaListSource, MediaListField>() {
    override var field: MediaListField = MediaListField()

    override fun createSource(): MediaListSource {
        source = MediaListSource(field, service, compositeDisposable)
        return source!!
    }

    fun increaseProgress(
        model: MediaListModel,
        callback: (Resource<MediaListModel>) -> Unit
    ) {

        // TODO INCREASE SERVICE
//        entryService.increaseProgress(model, compositeDisposable, callback)
    }

}