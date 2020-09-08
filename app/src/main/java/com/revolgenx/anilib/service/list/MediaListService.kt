package com.revolgenx.anilib.service.list

import com.revolgenx.anilib.field.list.MediaListCollectionField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface MediaListService {
    fun getMediaListCollection(
        field: MediaListCollectionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<MediaListModel>>) -> Unit
    )

    fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<MediaListModel>>) -> Unit
    )
}
