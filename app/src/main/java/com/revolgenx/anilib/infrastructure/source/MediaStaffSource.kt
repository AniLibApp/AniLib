package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.data.field.media.MediaStaffField
import com.revolgenx.anilib.data.model.MediaStaffModel
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable

class MediaStaffSource(
    field: MediaStaffField,
    private val browseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaStaffModel, MediaStaffField>(field) {

    override fun areItemsTheSame(first: MediaStaffModel, second: MediaStaffModel): Boolean {
        return first.staffId == second.staffId
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        browseService.getMediaStaff(field, compositeDisposable) { res ->
           postResult(page, res)
        }
    }
}
