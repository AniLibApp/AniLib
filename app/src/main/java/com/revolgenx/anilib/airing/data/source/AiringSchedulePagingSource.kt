package com.revolgenx.anilib.airing.data.source

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import kotlinx.coroutines.flow.single

class AiringSchedulePagingSource(
    field: AiringScheduleField,
    private val service: AiringScheduleService
) : BasePagingSource<AiringScheduleModel, AiringScheduleField>(field) {
    override suspend fun loadPage(): PageModel<AiringScheduleModel> {
        return service.getAiringSchedule(field).single()
    }
}