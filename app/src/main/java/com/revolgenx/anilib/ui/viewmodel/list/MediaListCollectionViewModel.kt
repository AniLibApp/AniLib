package com.revolgenx.anilib.ui.viewmodel.list

import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.entry.data.meta.EntryEditorResultMeta
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.entry.service.MediaEntryService
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListCollectionSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

abstract class MediaListCollectionViewModel(
    private val mediaListService: MediaListService,
    private val entryService: MediaEntryService
) :
    SourceViewModel<MediaListCollectionSource, MediaListCollectionField>() {
    val listMap by lazy { mutableMapOf<Int, AlMediaListModel>() }


    fun updateFilter(filter:MediaListCollectionFilterField){
        this.field.filter = filter
    }

    override fun createSource(): MediaListCollectionSource {
        if (source == null) {
            source =
                MediaListCollectionSource(
                    field,
                    listMap,
                    mediaListService,
                    compositeDisposable
                )
        } else {
            source!!.filterPage()
        }
        return source!!
    }


    fun increaseProgress(
        model: EntryListEditorMediaModel,
        callback: (Resource<EntryListEditorMediaModel>) -> Unit
    ) {
        entryService.increaseProgress(model, compositeDisposable, callback)
    }


    fun updateMediaListCollection(model: EntryEditorResultMeta){
        if (model.mediaId == null) {
            return
        }
        if(model.deleted){
            listMap.remove(model.mediaId)
        }else{
            listMap[model.mediaId]?.progress = model.progress
        }
    }

    override fun onCleared() {
        listMap.clear()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun renewSource() {
        source = null
        listMap.clear()
    }
}
