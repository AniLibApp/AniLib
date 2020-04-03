package com.revolgenx.anilib.service

import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable


interface AdvanceSearchService {
    fun search(
        field: BaseAdvanceSearchField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseModel>>) -> Unit)
    )
}
