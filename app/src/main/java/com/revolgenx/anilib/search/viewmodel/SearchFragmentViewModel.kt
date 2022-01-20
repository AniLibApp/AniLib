package com.revolgenx.anilib.search.viewmodel

import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.search.data.field.MediaSearchField
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.search.service.SearchService
import com.revolgenx.anilib.infrastructure.source.BrowseSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class SearchFragmentViewModel(private val searchService: SearchService) :
    SourceViewModel<BrowseSource, SearchField>() {

    override var field: SearchField = MediaSearchField().also {
        it.sort = MediaSort.TRENDING_DESC.ordinal
    }


    override fun createSource(): BrowseSource {
        source = BrowseSource(field, searchService, compositeDisposable)
        return source!!
    }

    var searchQuery: String = ""
    var genreTagFields: MutableList<TagField> = mutableListOf()
    var tagTagFields: MutableList<TagField> = mutableListOf()
    var streamTagFields: MutableList<TagField> = mutableListOf()


    override fun onCleared() {
        genreTagFields.clear()
        tagTagFields.clear()
        streamTagFields.clear()
        super.onCleared()
    }
}