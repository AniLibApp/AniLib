package com.revolgenx.anilib.media.viewmodel

import com.revolgenx.anilib.media.data.field.MediaSocialFollowingField
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.infrastructure.source.media.MediaSocialFollowingSource
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class MediaSocialFollowingViewModel(private val service: MediaInfoService) :
    SourceViewModel<MediaSocialFollowingSource, MediaSocialFollowingField>() {
    override var field: MediaSocialFollowingField = MediaSocialFollowingField()

    override fun createSource(): MediaSocialFollowingSource {
        source =  MediaSocialFollowingSource(field, service, compositeDisposable)
        return source!!
    }
}
