package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.search.SearchField
import com.revolgenx.anilib.field.search.MediaSearchField
import com.revolgenx.anilib.service.media.SearchService
import com.revolgenx.anilib.source.BrowseSource
import com.revolgenx.anilib.type.MediaSort

class SearchFragmentViewModel(private val searchService: SearchService) :
    SourceViewModel<BrowseSource, SearchField>() {

    override var field: SearchField = MediaSearchField().also {
        it.sort = MediaSort.TRENDING_DESC.ordinal
    }


    override fun createSource(): BrowseSource {
        source = BrowseSource(field, searchService, compositeDisposable)
        return source!!
    }

}
