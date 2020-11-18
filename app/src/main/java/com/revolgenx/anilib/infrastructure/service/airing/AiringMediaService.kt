package com.revolgenx.anilib.infrastructure.service.airing

import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface AiringMediaService {
    fun getAiringMedia(field: AiringMediaField, compositeDisposable: CompositeDisposable, callback:((items: Resource<List<AiringMediaModel>>)->Unit))
}
