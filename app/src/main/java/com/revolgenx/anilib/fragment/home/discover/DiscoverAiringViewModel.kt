package com.revolgenx.anilib.fragment.home.discover

import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.field.home.AiringMediaField
import com.revolgenx.anilib.service.airing.AiringMediaService
import com.revolgenx.anilib.source.home.airing.AiringSource
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.viewmodel.SourceViewModel

class DiscoverAiringViewModel(private val airingMediaService: AiringMediaService) :
    SourceViewModel<AiringSource, AiringMediaField>() {

    override var field: AiringMediaField = AiringMediaField()
        .also {
        it.sort = AiringSort.TIME.ordinal
    }

    var adapter: Adapter? = null

    override fun createSource(): AiringSource {
        source = AiringSource(field, airingMediaService, compositeDisposable)
        return source!!
    }


    override fun onCleared() {
        adapter?.releasePages()
        super.onCleared()
    }

}
