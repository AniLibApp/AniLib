package com.revolgenx.anilib.service.media

import com.revolgenx.anilib.field.search.SearchField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable


interface SearchService {
    fun search(
        field: SearchField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseModel>>) -> Unit)
    )
}
