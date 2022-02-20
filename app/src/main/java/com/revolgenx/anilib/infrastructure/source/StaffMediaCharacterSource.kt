package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.staff.service.StaffService
import io.reactivex.disposables.CompositeDisposable

class StaffMediaCharacterSource(
    field: StaffMediaCharacterField,
    private val staffService: StaffService,
    private val compositeDisposable: CompositeDisposable
) :
    BaseRecyclerSource<MediaModel, StaffMediaCharacterField>(field) {
    override fun areItemsTheSame(
        first: MediaModel,
        second: MediaModel
    ): Boolean {
        return first.id == second.id
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        staffService.getStaffMediaCharacter(field, compositeDisposable) { res ->
            postResult(page, res)
        }
    }

}
