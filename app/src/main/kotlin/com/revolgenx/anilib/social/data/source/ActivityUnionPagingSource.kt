package com.revolgenx.anilib.social.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.ui.model.ActivityModel
import kotlinx.coroutines.flow.single

class ActivityUnionPagingSource(
    field: ActivityUnionField,
    private val service: ActivityUnionService
) : BasePagingSource<ActivityModel, ActivityUnionField>(field) {
    override suspend fun loadPage(): PageModel<ActivityModel> {
        return service.getActivityUnion(field).single()
    }
}