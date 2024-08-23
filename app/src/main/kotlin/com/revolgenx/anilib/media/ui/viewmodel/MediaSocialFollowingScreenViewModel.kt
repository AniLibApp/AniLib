package com.revolgenx.anilib.media.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.media.data.field.MediaSocialFollowingField
import com.revolgenx.anilib.media.data.service.MediaService
import com.revolgenx.anilib.media.data.source.MediaSocialFollowingPagingSource
import com.revolgenx.anilib.media.ui.model.MediaSocialFollowingModel

class MediaSocialFollowingScreenViewModel(private val service: MediaService) : PagingViewModel<
        MediaSocialFollowingModel, MediaSocialFollowingField, MediaSocialFollowingPagingSource>() {

    override val field: MediaSocialFollowingField = MediaSocialFollowingField(-1)
    override val pagingSource: MediaSocialFollowingPagingSource
        get() = MediaSocialFollowingPagingSource(this.field, service)

}