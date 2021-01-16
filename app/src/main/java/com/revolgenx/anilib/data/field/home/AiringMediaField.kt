package com.revolgenx.anilib.data.field.home

import com.revolgenx.anilib.AiringScheduleQuery
import com.revolgenx.anilib.data.field.BaseSourceField
import com.revolgenx.anilib.data.field.BaseSourceUserField
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.type.MediaListStatus

class AiringMediaField : BaseSourceUserField<AiringScheduleQuery>() {
    var notYetAired = true
    var airingGreaterThan: Int? = null
    var airingLessThan: Int? = null
    var sort: Int? = null

    var showFromPlanning: Boolean = false
    var showFromWatching: Boolean = false

    var showFromPlanningOld = false
    var showFromWatchingOld = false
    var isNewField = true

    var mediaListIds: List<Int>? = null

    val dataChanged get() = 
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
        return AiringScheduleQuery.builder()
            .page(page)
            .perPage(perPage)

            .apply {
                airingGreaterThan?.let {
                    airingAtGreater(it)
                }
                airingLessThan?.let {
                    airingAtLesser(it)
                }
                if (notYetAired) {
                    notYetAired(notYetAired)
                }

                if(showFromWatching || showFromPlanning){
                    mediaListIds?.let {
                        mediaId_in(it)
                    }
                }

                sort?.let {
                    sort(listOf(AiringSort.values()[it]))
                }
            }
            .build()
    }

    fun updateOldField() {
        showFromPlanningOld = showFromPlanning
        showFromWatchingOld = showFromWatching
    }
}
