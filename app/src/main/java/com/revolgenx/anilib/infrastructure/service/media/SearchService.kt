package com.revolgenx.anilib.infrastructure.service.media

import com.revolgenx.anilib.data.field.search.SearchField
import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable


interface SearchService {
    fun search(
        field: SearchField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseModel>>) -> Unit)
    )
}
