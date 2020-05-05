package com.revolgenx.anilib.service.media

import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable


interface BrowseService {
    fun search(
        field: BrowseField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<BaseModel>>) -> Unit)
    )
}
