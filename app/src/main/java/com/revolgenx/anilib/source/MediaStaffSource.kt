package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.constant.PAGE_SIZE
import com.revolgenx.anilib.field.overview.MediaStaffField
import com.revolgenx.anilib.model.MediaStaffModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable
import kotlin.Exception

class MediaStaffSource(
    field: MediaStaffField,
    private val browseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaStaffModel,MediaStaffField>(field) {

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
