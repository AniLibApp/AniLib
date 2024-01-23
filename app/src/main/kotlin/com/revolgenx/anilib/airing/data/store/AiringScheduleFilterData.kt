package com.revolgenx.anilib.airing.data.store

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.type.AiringSort
import kotlinx.serialization.Serializable


@Serializable
data class AiringScheduleFilterData(
    val notYetAired: Boolean = true,
    val sort: AiringSort = AiringSort.TIME,
    val showOnlyWatching: Boolean = false,
    val showOnlyPlanning: Boolean = false,
    val isWeeklyTypeDate: Boolean = false,
) {
    fun toField(): AiringScheduleField {
        return AiringScheduleField(
            notYetAired = notYetAired,
            sort = sort,
            showOnlyPlanning = showOnlyPlanning,
            showOnlyWatching = showOnlyWatching,
            isWeeklyTypeDate = isWeeklyTypeDate,
        )
    }
}
