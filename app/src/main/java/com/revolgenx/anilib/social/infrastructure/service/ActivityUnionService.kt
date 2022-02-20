package com.revolgenx.anilib.social.infrastructure.service

import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.social.data.field.*
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
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

    fun saveTextActivity(
        field:SaveTextActivityField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Int>) -> Unit
    )

    fun saveMessageActivity(
        field:SaveMessageActivityField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Int>) -> Unit
    )

    fun saveActivityReply(
        field:SaveActivityReplyField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<ActivityReplyModel>) -> Unit
    )

    fun deleteActivityReply(
        field:DeleteActivityReplyField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Boolean>) -> Unit
    )
}