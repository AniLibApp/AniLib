package com.revolgenx.anilib.airing.service

import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.airing.data.model.AiringMediaModel
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface AiringMediaService {
    fun getAiringMedia(field: AiringMediaField, compositeDisposable: CompositeDisposable, callback:((items: Resource<List<AiringScheduleModel>>)->Unit))
}
