package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.data.source.ActivityUnionPagingSource
import com.revolgenx.anilib.social.ui.model.ActivityUnionModel

class ActivityUnionViewModel(private val activityUnionService: ActivityUnionService) :
    PagingViewModel<ActivityUnionModel, ActivityUnionField, ActivityUnionPagingSource>() {

    override val field: ActivityUnionField = ActivityUnionField()
    override val pagingSource: ActivityUnionPagingSource
        get() = ActivityUnionPagingSource(this.field, activityUnionService)

}