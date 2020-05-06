package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.field.browse.MediaBrowseField
import com.revolgenx.anilib.service.media.BrowseService
import com.revolgenx.anilib.source.BrowseSource
import com.revolgenx.anilib.type.MediaSort

class BrowseFragmentViewModel(private val browseService: BrowseService) :
    SourceViewModel<BrowseSource, BrowseField>() {

    override var field: BrowseField = MediaBrowseField().also {
        it.sort = MediaSort.TRENDING_DESC.ordinal
    }


    override fun createSource(): BrowseSource {
        source = BrowseSource(field, browseService, compositeDisposable)
        return source!!
    }

}
