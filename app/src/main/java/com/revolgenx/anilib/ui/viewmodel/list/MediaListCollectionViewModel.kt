package com.revolgenx.anilib.ui.viewmodel.list

import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.infrastructure.service.media.MediaListEntryService
import com.revolgenx.anilib.infrastructure.source.media_list.MediaListCollectionSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

/*
* todo addmedialist might not update on rotaion rare case
* */
abstract class MediaListCollectionViewModel(
    private val mediaListService: MediaListService,
    private val entryService: MediaListEntryService
) :
    SourceViewModel<MediaListCollectionSource, MediaListCollectionField>() {
    var filteredList: MutableList<MediaListModel>? = null
    val listMap by lazy { mutableMapOf<Int, MediaListModel>() }


    fun updateFilter(filter:MediaListCollectionFilterField){
        this.field.filter = filter
    }

    override fun createSource(): MediaListCollectionSource {
        filteredList = null
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


    override fun onCleared() {
        listMap.clear()
        filteredList = null
        compositeDisposable.clear()
        super.onCleared()
    }

    fun renewSource() {
        source = null
        filteredList = null
        listMap.clear()
    }
}
