package com.revolgenx.anilib.viewmodel

import com.otaliastudios.elements.Source
import com.revolgenx.anilib.field.MediaListFilterField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.service.list.MediaListService
import com.revolgenx.anilib.source.MediaListSource

abstract class MediaListViewModel(protected val mediaListService: MediaListService) :
    SourceViewModel<MediaListSource, MediaListField>() {
    var filteredList: MutableList<MediaListModel>? = null
    val list by lazy { mutableListOf<MediaListModel>() }

    fun filterList(filter: MediaListFilterField, field: MediaListField): MediaListSource? {
        if (list.isEmpty() || filter.isNull()) return null
        filteredList = null

        filteredList = list.filter {
                filter.search?.let { str ->
                    it.title?.userPreferred?.contains(str ?: "")

                } == true || filter.format?.let { frmt ->
                    it.format == frmt
                } == true || filter.status?.let { sta ->
                    it.status == sta
                } == true || filter.genre?.let { gen ->
                    it.genres?.contains(gen)
                } == true
            }.toMutableList()
        source = MediaListSource(field, filteredList!!, mediaListService, compositeDisposable)
        return source
    }
}
