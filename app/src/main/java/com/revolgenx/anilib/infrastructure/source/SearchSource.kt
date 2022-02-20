package com.revolgenx.anilib.infrastructure.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.search.service.SearchService
import io.reactivex.disposables.CompositeDisposable

class SearchSource(
    field: SearchField,
    private val searchService: SearchService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<BaseModel, SearchField>(field) {
    override fun areItemsTheSame(first: BaseModel, second: BaseModel): Boolean {
        return first.id == second.id
    }

    override fun getElementType(data: BaseModel): Int {
        return field.searchType.ordinal
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        searchService.search(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
