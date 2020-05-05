package com.revolgenx.anilib.fragment.home.discover

import com.otaliastudios.elements.Adapter
import com.revolgenx.anilib.field.home.airing.AiringMediaField
import com.revolgenx.anilib.service.airing.AiringMediaService
import com.revolgenx.anilib.source.home.airing.AiringSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class DiscoverAiringViewModel(private val airingMediaService: AiringMediaService) :
    SourceViewModel<AiringSource, AiringMediaField>() {

    val field = AiringMediaField()
    var adapter: Adapter? = null

    override fun createSource(field: AiringMediaField): AiringSource {
        source = AiringSource(field, airingMediaService, compositeDisposable)
        return source!!
    }


    override fun onCleared() {
        adapter?.releasePages()
        super.onCleared()
    }

}
