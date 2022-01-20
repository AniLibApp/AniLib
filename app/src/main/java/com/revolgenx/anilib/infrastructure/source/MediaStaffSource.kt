package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.staff.data.model.StaffEdgeModel
import io.reactivex.disposables.CompositeDisposable

class MediaStaffSource(
    field: MediaStaffField,
    private val browseService: MediaInfoService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<StaffEdgeModel, MediaStaffField>(field) {

    override fun areItemsTheSame(first: StaffEdgeModel, second: StaffEdgeModel): Boolean {
        return first.node?.id == second.node?.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        browseService.getMediaStaff(field, compositeDisposable) { res ->
           postResult(page, res)
        }
    }
}
