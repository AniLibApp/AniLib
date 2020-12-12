package com.revolgenx.anilib.ui.viewmodel.home.discover

import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.discover.DiscoverMediaSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

abstract class BaseDiscoverViewModel<F : MediaField>(private val mediaService: MediaService) :
    SourceViewModel<DiscoverMediaSource, F>() {

    override fun createSource(): DiscoverMediaSource {
        source = DiscoverMediaSource(mediaService, field, compositeDisposable)
        return source!!
    }
}