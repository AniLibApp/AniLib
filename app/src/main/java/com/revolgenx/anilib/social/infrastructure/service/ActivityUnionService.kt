package com.revolgenx.anilib.social.infrastructure.service

import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import io.reactivex.disposables.CompositeDisposable

interface ActivityUnionService {
    fun getActivityUnion(
        field: ActivityUnionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<ActivityUnionModel>>) -> Unit)
    )
}