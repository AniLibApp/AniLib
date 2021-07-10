package com.revolgenx.anilib.ui.viewmodel.media

import com.revolgenx.anilib.data.field.media.MediaSocialFollowingField
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import com.revolgenx.anilib.infrastructure.source.media.MediaSocialFollowingSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class MediaSocialFollowingViewModel(private val service: MediaBrowseService) :
    SourceViewModel<MediaSocialFollowingSource, MediaSocialFollowingField>() {
    override var field: MediaSocialFollowingField = MediaSocialFollowingField()

    override fun createSource(): MediaSocialFollowingSource {
        source =  MediaSocialFollowingSource(field, service, compositeDisposable)
        return source!!
    }
}
