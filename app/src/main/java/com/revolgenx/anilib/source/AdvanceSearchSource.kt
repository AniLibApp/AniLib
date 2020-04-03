package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.service.AdvanceSearchService
import io.reactivex.disposables.CompositeDisposable

class AdvanceSearchSource(
    field: BaseAdvanceSearchField,
    private val advanceSearchService: AdvanceSearchService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<BaseModel, BaseAdvanceSearchField>(field) {
    override fun areItemsTheSame(first: BaseModel, second: BaseModel): Boolean {
        return first.baseId == second.baseId
    }

    override fun getElementType(data: BaseModel): Int {
        return field.type
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        advanceSearchService.search(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
