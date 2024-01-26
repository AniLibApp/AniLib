package com.revolgenx.anilib.social.data.source

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import kotlinx.coroutines.flow.single

class ActivityReplyPagingSource(
    field: ActivityReplyField,
    private val service: ActivityUnionService
) : BasePagingSource<ActivityReplyModel, ActivityReplyField>(field) {
    override suspend fun loadPage(): PageModel<ActivityReplyModel> {
        return service.getActivityReply(field).single()
    }
}