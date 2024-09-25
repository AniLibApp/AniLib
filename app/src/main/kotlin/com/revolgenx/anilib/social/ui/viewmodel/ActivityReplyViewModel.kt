package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.data.source.ActivityReplyPagingSource
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel

class ActivityReplyViewModel(
    activityId: Int,
    private val activityUnionService: ActivityUnionService
) : PagingViewModel<ActivityReplyModel, ActivityReplyField, ActivityReplyPagingSource>() {

    override val field: ActivityReplyField = ActivityReplyField(activityId = activityId)
    override val pagingSource: ActivityReplyPagingSource
        get() = ActivityReplyPagingSource(field = this.field, service = activityUnionService)

}