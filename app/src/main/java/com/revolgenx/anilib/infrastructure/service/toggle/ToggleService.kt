package com.revolgenx.anilib.infrastructure.service.toggle

import com.revolgenx.anilib.data.model.toggle.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import io.reactivex.disposables.CompositeDisposable

interface ToggleService {
    fun toggleLikeV2(
        field: ToggleLikeV2Field,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<LikeableUnionModel>) -> Unit
    )

    fun toggleActivitySubscription(
        field: ToggleActivitySubscriptionField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<ActivityUnionModel>) -> Unit
    )
}