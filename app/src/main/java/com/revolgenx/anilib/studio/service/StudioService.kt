package com.revolgenx.anilib.studio.service

import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.studio.data.field.StudioField
import com.revolgenx.anilib.studio.data.field.StudioMediaField
import com.revolgenx.anilib.studio.data.model.StudioModel
import io.reactivex.disposables.CompositeDisposable

interface StudioService {
    fun getStudioInfo(
        field: StudioField,
        compositeDisposable: CompositeDisposable,
        callback:(Resource<StudioModel>)->Unit
    )

    fun getStudioMedia(
        field: StudioMediaField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<MediaModel>>) -> Unit)
    )
}