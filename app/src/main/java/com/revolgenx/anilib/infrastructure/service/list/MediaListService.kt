package com.revolgenx.anilib.infrastructure.service.list

import com.revolgenx.anilib.data.field.list.*
import com.revolgenx.anilib.data.model.list.MediaListCountTypeModel
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface MediaListService {
    fun getMediaListCollection(
        field: MediaListCollectionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<AlMediaListModel>>) -> Unit
    )

    fun getMediaListCollectionIds(
        field: MediaListCollectionIdsField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<Int>>) -> Unit
    )

    fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<AlMediaListModel>>) -> Unit
    )

}