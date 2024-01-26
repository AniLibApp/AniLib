package com.revolgenx.anilib.social.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.social.ui.model.ActivityUnionModel
import kotlinx.coroutines.flow.Flow

interface ActivityUnionService {
    fun getActivityUnion(field: ActivityUnionField): Flow<PageModel<ActivityUnionModel>>
    fun getActivityReply(field: ActivityReplyField): Flow<PageModel<ActivityReplyModel>>

}