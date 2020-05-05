package com.revolgenx.anilib.service.media

import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface MediaService {

    fun getMedia(
        field: MediaField,
        compositeDisposable: CompositeDisposable,
        callback: ((Resource<List<CommonMediaModel>>)->Unit)
    )
}