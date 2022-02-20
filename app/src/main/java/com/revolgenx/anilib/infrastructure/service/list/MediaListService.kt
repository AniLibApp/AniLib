package com.revolgenx.anilib.infrastructure.service.list

import com.revolgenx.anilib.data.field.list.*
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.list.data.model.MediaListModel
import io.reactivex.disposables.CompositeDisposable

interface MediaListService {
    fun getMediaListCollectionIds(
        field: MediaListCollectionIdsField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<Int>>) -> Unit
    )

    fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<MediaListModel>>) -> Unit
    )

}