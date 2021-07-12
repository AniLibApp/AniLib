package com.revolgenx.anilib.infrastructure.source.media

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.media.MediaSocialFollowingField
import com.revolgenx.anilib.data.model.media_info.MediaSocialFollowingModel
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable

class MediaSocialFollowingSource(
    field: MediaSocialFollowingField,
    private val service: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaSocialFollowingModel, MediaSocialFollowingField>(field) {
    override fun areItemsTheSame(
        first: MediaSocialFollowingModel,
        second: MediaSocialFollowingModel
    ): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        service.getMediaSocialFollowing(field, compositeDisposable) {
            postResult(page, it)
        }
    }

}