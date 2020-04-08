package com.revolgenx.anilib.service.list

import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface MediaListService {
    fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<MediaListModel>>) -> Unit
    )
}
