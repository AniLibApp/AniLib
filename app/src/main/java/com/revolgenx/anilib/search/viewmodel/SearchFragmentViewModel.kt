package com.revolgenx.anilib.search.viewmodel

import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.search.service.SearchService
import com.revolgenx.anilib.infrastructure.source.SearchSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class SearchFragmentViewModel(private val searchService: SearchService) :
    SourceViewModel<SearchSource, SearchField>() {
    override var field: SearchField = SearchField()

    override fun createSource(): SearchSource {
        source = SearchSource(field, searchService, compositeDisposable)
        return source!!
    }

}