package com.revolgenx.anilib.infrastructure.service.media

import com.revolgenx.anilib.data.field.media.MediaField
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface MediaService {

    fun getMedia(
        field: MediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<CommonMediaModel>>)->Unit)
    )
}