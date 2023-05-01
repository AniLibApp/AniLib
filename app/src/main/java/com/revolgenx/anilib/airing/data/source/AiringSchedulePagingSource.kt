package com.revolgenx.anilib.airing.data.source

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.model.AiringScheduleHeaderModel
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.airing.ui.model.BaseAiringScheduleModel
import com.revolgenx.anilib.common.data.field.BaseSourceField
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.source.BasePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single


abstract class AiringSchedulePagingSource<M : Any, F : BaseSourceField<*>>(
    field: F
) : BasePagingSource<M, F>(field)

class AiringScheduleDailyPagingSource(
    field: AiringScheduleField,
    private val service: AiringScheduleService
) : AiringSchedulePagingSource<BaseAiringScheduleModel, AiringScheduleField>(field) {
    override suspend fun loadPage(): PageModel<BaseAiringScheduleModel> {
        return service.getAiringSchedule(field).single() as PageModel<BaseAiringScheduleModel>
    }
}


class AiringScheduleWeeklyPagingSource(
    field: AiringScheduleField,
    private val service: AiringScheduleService
) : AiringSchedulePagingSource<BaseAiringScheduleModel, AiringScheduleField>(field) {
    private var lastHeader: String = ""
    override suspend fun loadPage(): PageModel<BaseAiringScheduleModel> {
        return service.getAiringSchedule(field).map {
            val results = arrayListOf<BaseAiringScheduleModel>()
            for (airingMediaModel in it.data ?: emptyList()) {
                val header =
                    airingMediaModel.airingAtModel!!.airingDay

                if (header != lastHeader) {
                    results.add(AiringScheduleHeaderModel(header))
                    lastHeader = header
                }
                results.add(airingMediaModel)
            }
            PageModel(it.pageInfo, results)
        }.flowOn(Dispatchers.IO).single()
    }
}