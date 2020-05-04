package com.revolgenx.anilib.service.airing

import com.revolgenx.anilib.field.home.airing.AiringMediaField
import com.revolgenx.anilib.model.airing.AiringMediaModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface AiringMediaService {
    fun getAiringMedia(field:AiringMediaField, compositeDisposable: CompositeDisposable, callback:((items: Resource<List<AiringMediaModel>>)->Unit))
}
