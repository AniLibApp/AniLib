package com.revolgenx.anilib.social.infrastructure.service

import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.ActivityDeleteField
import com.revolgenx.anilib.social.data.field.ActivityInfoField
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import io.reactivex.disposables.CompositeDisposable

interface ActivityUnionService {
    fun getActivityUnion(
        field: ActivityUnionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<ActivityUnionModel>>) -> Unit)
    )

    fun getActivityInfo(
        field:ActivityInfoField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<ActivityUnionModel>) -> Unit
    )

    fun deleteActivity(
        field:ActivityDeleteField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Boolean>) -> Unit
    )
}