package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.field.MediaListFilterField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.source.MediaListSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class MediaListViewModel(private val mediaListService: MediaListService) :
    SourceViewModel<MediaListSource, MediaListField>() {
    var filteredList: MutableList<MediaListModel>? = null
    val list by lazy { mutableListOf<MediaListModel>() }
    var filter = MediaListFilterField()

    override fun createSource(field: MediaListField): MediaListSource {
        filteredList = null
        if (source == null) {
            source = MediaListSource(field, list, mediaListService, compositeDisposable)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                filteredList = if (filter.format == null) list else {
                    list.filter { it.format == filter.format }
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

    override fun onCleared() {
        list.clear()
        filteredList = null
        compositeDisposable.clear()
        super.onCleared()
    }
}
