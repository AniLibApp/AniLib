package com.revolgenx.anilib.ui.viewmodel.search

import com.revolgenx.anilib.data.field.search.SearchField
import com.revolgenx.anilib.data.field.search.MediaSearchField
import com.revolgenx.anilib.infrastructure.service.media.SearchService
import com.revolgenx.anilib.infrastructure.source.BrowseSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

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
