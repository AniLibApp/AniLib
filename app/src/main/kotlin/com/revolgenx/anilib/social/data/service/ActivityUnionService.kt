package com.revolgenx.anilib.social.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.field.SaveActivityReplyField
import com.revolgenx.anilib.social.data.field.SaveMessageActivityField
import com.revolgenx.anilib.social.data.field.SaveTextActivityField
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import kotlinx.coroutines.flow.Flow

interface ActivityUnionService {
    fun getActivityUnion(field: ActivityUnionField): Flow<PageModel<ActivityModel>>
    fun getActivityReply(field: ActivityReplyField): Flow<PageModel<ActivityReplyModel>>
    fun saveTextActivity(field: SaveTextActivityField): Flow<Int?>
    fun saveActivityReply(field: SaveActivityReplyField): Flow<Int?>
    fun saveMessageActivity(field: SaveMessageActivityField): Flow<Int?>
    fun deleteActivity(id: Int): Flow<Boolean>
    fun deleteActivityReply(id: Int): Flow<Boolean>
}