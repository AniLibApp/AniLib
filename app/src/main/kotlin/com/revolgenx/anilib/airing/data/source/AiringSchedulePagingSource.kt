package com.revolgenx.anilib.airing.data.source

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.HeaderModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single


class AiringSchedulePagingSource(
    field: AiringScheduleField,
    private val service: AiringScheduleService
) : BasePagingSource<BaseModel, AiringScheduleField>(field) {
    private var lastHeader: String = ""
    override suspend fun loadPage(): PageModel<BaseModel> {
        return service.getAiringSchedule(field).map {
            if (field.isWeeklyTypeDate) {
                val results = mutableListOf<BaseModel>()
                for (airingMediaModel in it.data.orEmpty()) {
                    val header =
                        airingMediaModel.airingAtModel.airingDay

                    if (header != lastHeader) {
                        results.add(HeaderModel(header))
                        lastHeader = header
                    }
                    results.add(airingMediaModel)
                }
                PageModel(it.pageInfo, results)
            } else it
        }.single() as PageModel<BaseModel>
    }
}