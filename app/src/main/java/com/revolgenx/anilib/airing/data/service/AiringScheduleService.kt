package com.revolgenx.anilib.airing.data.service

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.common.data.model.PageModel
import kotlinx.coroutines.flow.Flow

interface AiringScheduleService {
    fun getAiringSchedule(field: AiringScheduleField): Flow<PageModel<AiringScheduleModel>>
}