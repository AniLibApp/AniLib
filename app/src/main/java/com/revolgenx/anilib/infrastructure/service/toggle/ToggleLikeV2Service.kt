package com.revolgenx.anilib.infrastructure.service.toggle

import com.revolgenx.anilib.data.model.toggle.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import io.reactivex.disposables.CompositeDisposable

interface ToggleLikeV2Service {
    fun toggleLike(
        field: ToggleLikeV2Field,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<LikeableUnionModel>) -> Unit
    )
}