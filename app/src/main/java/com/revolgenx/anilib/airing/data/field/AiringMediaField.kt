package com.revolgenx.anilib.airing.data.field

import com.revolgenx.anilib.AiringScheduleQuery
import com.revolgenx.anilib.common.data.field.BaseSourceUserField
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.type.MediaListStatus

open class AiringMediaField : BaseSourceUserField<AiringScheduleQuery>() {
    var notYetAired = true
    var airingGreaterThan: Int? = null
    var airingLessThan: Int? = null
    var sort: Int? = null
    var sorts: List<Int>? = null

    var showFromPlanning: Boolean = false
    var showFromWatching: Boolean = false

    var showFromPlanningOld = false
    var showFromWatchingOld = false
    var isNewField = true

    var mediaListIds: List<Int>? = null

    var isWeeklyTypeDate: Boolean = false

    val dataChanged
        get() =
            showFromPlanning != showFromPlanningOld || showFromWatching != showFromWatchingOld

    val mediaListStatus
        get() = mutableListOf<Int>().also {
            if (showFromWatching) {
                it.add(MediaListStatus.CURRENT.ordinal)
            }
            if (showFromPlanning) {
                it.add(MediaListStatus.PLANNING.ordinal)
            }
        }

    override fun toQueryOrMutation(): AiringScheduleQuery {
        val listIds = if(showFromWatching || showFromPlanning){
            mediaListIds
        }else null

        val airingSort = if (isWeeklyTypeDate) {
            if (sort != null) {
                listOf(AiringSort.TIME, AiringSort.values()[sort!!])
            } else {
                listOf(AiringSort.TIME)
            }
        } else {
            sort?.let {
                listOf(AiringSort.values()[it])
            }
        }

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

    fun updateOldField() {
        showFromPlanningOld = showFromPlanning
        showFromWatchingOld = showFromWatching
    }
}
