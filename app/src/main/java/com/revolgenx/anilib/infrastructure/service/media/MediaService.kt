package com.revolgenx.anilib.infrastructure.service.media

import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import io.reactivex.disposables.CompositeDisposable

interface MediaService {

    fun getMedia(
        field: MediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<MediaModel>>) -> Unit)
    )

    fun getSelectableMedia(
        field: MediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<MediaModel>>) -> Unit)
    )
}