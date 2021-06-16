package com.revolgenx.anilib.social.ui.viewmodel

import android.content.Context
import com.revolgenx.anilib.common.preference.getActivityUnionField
import com.revolgenx.anilib.common.preference.storeActivityUnionField
import com.revolgenx.anilib.data.model.toggle.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.social.data.field.ActivityDeleteField
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.infrastructure.service.ActivityUnionService
import com.revolgenx.anilib.social.infrastructure.source.ActivityUnionSource
import com.revolgenx.anilib.type.LikeableType
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class ActivityUnionViewModel(
    private val context: Context,
    private val activityUnionService: ActivityUnionService,
    private val toggleService: ToggleService
) :
    SourceViewModel<ActivityUnionSource, ActivityUnionField>() {
    override var field: ActivityUnionField = getActivityUnionField(context)


    override fun createSource(): ActivityUnionSource {
        source = ActivityUnionSource(field, activityUnionService, compositeDisposable)
        return source!!
    }


    fun storeField() {
        storeActivityUnionField(context, field)
    }

    fun toggleActivityLike(
        item: ActivityUnionModel,
        callback: (Resource<LikeableUnionModel>) -> Unit
    ) {
        toggleService.toggleLikeV2(ToggleLikeV2Field().also {
            it.id = item.id
            it.type = LikeableType.ACTIVITY
        }, compositeDisposable) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data ?: return@toggleLikeV2
                    item.likeCount = data.likeCount
                    item.isLiked = data.isLiked
                    item.onDataChanged?.invoke()
                }
                else -> {
                }
            }
            callback.invoke(it)
        }
    }

    fun toggleActivitySubscription(
        item: ActivityUnionModel,
        callback: (Resource<ActivityUnionModel>) -> Unit
    ) {
        toggleService.toggleActivitySubscription(ToggleActivitySubscriptionField().also {
            it.activityId = item.id
            it.isSubscribed = !item.isSubscribed
        }, compositeDisposable) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data ?: return@toggleActivitySubscription
                    item.isSubscribed = data.isSubscribed
                    item.onDataChanged?.invoke()
                }
            }
            callback.invoke(it)
        }
    }

    fun deleteActivity(id: Int, callback: (id: Int, b: Boolean) -> Unit) {
        activityUnionService.deleteActivity(ActivityDeleteField().also {
            it.activityId = id
        }, compositeDisposable) {
            callback.invoke(
                id,
                if (it.status == Status.SUCCESS) {
                    it.data!!
                } else {
                    false
                }
            )

        }
    }

}