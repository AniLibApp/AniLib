package com.revolgenx.anilib.widget.viewmodel

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.type.AiringSort
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import java.time.ZonedDateTime

class AiringWidgetResource(
    private val airingScheduleService: AiringScheduleService,
    private val appPreferencesDataStore: AppPreferencesDataStore
) : ResourceViewModel<PageModel<AiringScheduleModel>, AiringScheduleField>() {

    suspend fun watchSettings(){
        with(appPreferencesDataStore) {
            launch {
                widgetAiringSort.collect {
                    val airingSort = AiringSort.entries[it!!]
                    if(field.sort != airingSort){
                        field.sort = airingSort
                        refresh()
                    }
                }
            }

            launch {
                widgetOnlyPlanning.collect {
                    if(field.showOnlyPlanning != it){
                        field.needMediaListData = needMediaListData(showOnlyWatching = field.showOnlyWatching, showOnlyPlanning = it!!)
                        field.showOnlyPlanning = it
                        refresh()
                    }
                }
            }

            launch {
                widgetOnlyWatching.collect {
                    if(field.showOnlyWatching != it){
                        field.needMediaListData = needMediaListData(showOnlyWatching = it!!, showOnlyPlanning = field.showOnlyPlanning)
                        field.showOnlyWatching = it
                        refresh()
                    }
                }
            }

            launch {
                widgetIncludeAlreadyAired.collect {
                    if(field.notYetAired != !it!!){
                        field.notYetAired = !it
                        refresh()
                    }
                }
            }
        }
    }

    override fun refresh() {
        changeDateIfDifferentDay()
        super.refresh()
    }

    private val todayStartDateTime get() = ZonedDateTime.now().with(LocalTime.MIN)
    private val todayEndDateTime get() = ZonedDateTime.now().with(LocalTime.MAX)

    var startDateTime = todayStartDateTime
    var endDateTime = todayEndDateTime

    override val field: AiringScheduleField = with(appPreferencesDataStore) {
        AiringScheduleField(
            airingGreaterThan = startDateTime.toEpochSecond().toInt(),
            airingLessThan = endDateTime.toEpochSecond().toInt(),
            showOnlyPlanning = widgetOnlyPlanning.get()!!,
            showOnlyWatching = widgetOnlyWatching.get()!!,
            notYetAired = !widgetIncludeAlreadyAired.get()!!,
            sort = AiringSort.entries[widgetAiringSort.get()!!]
        ).also {
            it.userId = userId.get()
        }
    }

    override fun load(): Flow<PageModel<AiringScheduleModel>?> {
        return airingScheduleService.getAiringSchedule(field)
    }

    private fun changeDateIfDifferentDay() {
        if (todayStartDateTime != startDateTime) {
            startDateTime = todayStartDateTime
            endDateTime = todayEndDateTime
            field.airingGreaterThan = startDateTime.toEpochSecond().toInt()
            field.airingLessThan = endDateTime.toEpochSecond().toInt()
        }
    }

    private fun needMediaListData(showOnlyWatching: Boolean, showOnlyPlanning: Boolean) =
        ((field.showOnlyPlanning || field.showOnlyWatching)
            && (field.showOnlyWatching != showOnlyWatching
                    || field.showOnlyPlanning != showOnlyPlanning)
        )

}