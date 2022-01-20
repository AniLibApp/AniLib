package com.revolgenx.anilib.home.discover.viewmodel

import com.revolgenx.anilib.infrastructure.service.media.MediaService
import com.revolgenx.anilib.infrastructure.source.discover.DiscoverMediaSource
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

abstract class BaseDiscoverViewModel<F : MediaField>(private val mediaService: MediaService) :
    SourceViewModel<DiscoverMediaSource, F>() {

    override fun createSource(): DiscoverMediaSource {
        source = DiscoverMediaSource(mediaService, field, compositeDisposable)
        return source!!
    }
}