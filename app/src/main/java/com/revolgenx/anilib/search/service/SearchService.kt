package com.revolgenx.anilib.search.service

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.search.data.field.SearchField
import io.reactivex.disposables.CompositeDisposable

interface SearchService {
    fun search(
        field: SearchField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseModel>>) -> Unit)
    )
}