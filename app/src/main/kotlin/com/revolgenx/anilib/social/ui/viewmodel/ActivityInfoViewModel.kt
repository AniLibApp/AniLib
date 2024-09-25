package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.social.data.field.ActivityInfoField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.ui.model.ActivityModel
import kotlinx.coroutines.flow.Flow

class ActivityInfoViewModel(
    val activityId: Int,
    private val activityUnionService: ActivityUnionService
) :
    ResourceViewModel<ActivityModel, ActivityInfoField>() {

    override val field: ActivityInfoField = ActivityInfoField(activityId)

    override fun load(): Flow<ActivityModel?> {
        return activityUnionService.getActivityInfo(field)
    }

}