package com.revolgenx.anilib.airing.data.field

import com.revolgenx.anilib.AiringScheduleQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.type.MediaListStatus

data class AiringScheduleField(
    var notYetAired: Boolean = true,
    val airingGreaterThan: Int? = null,
    val airingLessThan: Int? = null,
    var sort: AiringSort = AiringSort.TIME,
    var showOnlyWatching: Boolean = false,
    var showOnlyPlanning: Boolean = false,
    var isWeeklyTypeDate: Boolean = false,
    var mediaListIds: List<Int>? = null,
    var needMediaListData: Boolean = false
) : BaseSourceUserField<AiringScheduleQuery>() {

    val mediaListStatus
        get() = listOfNotNull(
            MediaListStatus.CURRENT.takeIf { showOnlyWatching },
            MediaListStatus.PLANNING.takeIf { showOnlyPlanning },
        )


    override fun toQueryOrMutation(): AiringScheduleQuery {
        val listIds = mediaListIds.takeIf { showOnlyWatching || showOnlyPlanning }
        val airingSort = listOfNotNull(AiringSort.TIME.takeIf { isWeeklyTypeDate }, sort)

        return AiringScheduleQuery(
            page = nn(page),
            perPage = nn(perPage),
            airingAtGreater = nn(airingGreaterThan),
            airingAtLesser = nn(airingLessThan),
            notYetAired = nnBool(notYetAired),
            mediaId_in = nn(listIds),
            sort = nn(airingSort)
        )
    }

}