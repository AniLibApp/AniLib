package com.revolgenx.anilib.viewmodel.search

import com.revolgenx.anilib.field.search.SearchField
import com.revolgenx.anilib.field.search.MediaSearchField
import com.revolgenx.anilib.service.media.SearchService
import com.revolgenx.anilib.source.BrowseSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.viewmodel.SourceViewModel

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
