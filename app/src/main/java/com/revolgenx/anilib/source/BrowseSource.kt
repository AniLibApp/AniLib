package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.service.media.BrowseService
import io.reactivex.disposables.CompositeDisposable

class BrowseSource(
    field: BrowseField,
    private val browseService: BrowseService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<BaseModel, BrowseField>(field) {
    override fun areItemsTheSame(first: BaseModel, second: BaseModel): Boolean {
        return first.baseId == second.baseId
    }

    override fun getElementType(data: BaseModel): Int {
        return field.type
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        browseService.search(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
