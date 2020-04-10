package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.field.MediaListFilterField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.MediaListEntryService
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.source.MediaListSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
* todo addmedialist might not update on rotaion rare case
* todo create proper observer not stable for increasing progress
* */
abstract class MediaListViewModel(
    private val mediaListService: MediaListService,
    private val entryService: MediaListEntryService
) :
    SourceViewModel<MediaListSource, MediaListField>() {
    var filteredList: MutableList<MediaListModel>? = null
    val listMap by lazy { mutableMapOf<Int, MediaListModel>() }
    var filter = MediaListFilterField()

    private var mCallback: ((Resource<EntryListEditorMediaModel>) -> Unit)? = null

    fun addMediaListUpdateObserver(viewLifecycleOwner: LifecycleOwner) {
        entryService.saveMediaListEntryLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    listMap[res.data?.mediaId]?.progress =
                        res.data?.progress?.toString()
                }
            }
            mCallback?.invoke(res)
        }
    }

    fun removeObserver(viewLifecycleOwner: LifecycleOwner){
        entryService.saveMediaListEntryLiveData.removeObservers(viewLifecycleOwner)
    }

    override fun createSource(field: MediaListField): MediaListSource {
        filteredList = null
        if (source == null) {
            source = MediaListSource(field, listMap, mediaListService, compositeDisposable)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                filteredList = if (filter.format == null) listMap.values else {
                    listMap.values.filter { it.format == filter.format }
                }.let {
                    if (filter.status == null) it else it.filter { it.status == filter.status }
                }.let {
                    if (filter.genre == null) it else it.filter { it.genres?.contains(filter.genre!!) == true }
                }.let {
                    if (filter.search.isNullOrEmpty()) it else {
                        it.filter {
                            it.title?.userPreferred?.contains(
                                filter.search!!, true
                            ) == true
                        }
                    }
                }.toMutableList()
                launch(Dispatchers.Main) {
                    source?.filterPage(filteredList!!)
                }
            }
        }
        return source!!
    }


    fun saveMediaListEntry(
        model: EntryListEditorMediaModel,
        callback: (Resource<EntryListEditorMediaModel>) -> Unit
    ) {
        mCallback = callback
        entryService.saveMediaListEntry(
            model,
            compositeDisposable
        )
    }

    override fun onCleared() {
        listMap.clear()
        filteredList = null
        compositeDisposable.clear()
        super.onCleared()
    }
}
