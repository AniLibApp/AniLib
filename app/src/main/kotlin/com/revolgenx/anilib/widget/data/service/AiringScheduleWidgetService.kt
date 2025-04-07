package com.revolgenx.anilib.widget.data.service

import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.data.service.AiringScheduleService
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.common.data.store.AiringScheduleWidgetDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.widget.data.store.AiringScheduleWidgetData
import com.revolgenx.anilib.widget.data.store.toData
import com.revolgenx.anilib.widget.data.store.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalTime
import java.time.ZonedDateTime

class AiringScheduleWidgetService(
    private val airingScheduleService: AiringScheduleService,
    private val appPreferencesDataStore: AppPreferencesDataStore,
    private val airingScheduleWidgetDataStore: AiringScheduleWidgetDataStore
) {

    fun getAiringScheduleData(): Flow<List<AiringScheduleModel>?> {
        return airingScheduleWidgetDataStore.data.map {
            it.data?.map { it.toModel() }
        }
    }

    suspend fun removeAiringScheduleData(){
        airingScheduleWidgetDataStore.updateData {
            AiringScheduleWidgetData(data = null)
        }
    }

    fun getAiringSchedule(): Flow<List<AiringScheduleModel>?> {
        val startDateTime = ZonedDateTime.now().with(LocalTime.MIN)
        val endDateTime = ZonedDateTime.now().with(LocalTime.MAX)

        val field: AiringScheduleField = with(appPreferencesDataStore) {
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

        return airingScheduleService.getAiringSchedule(field).map { it.data }
            .onEach { airingSchedulePageModel ->
                airingScheduleWidgetDataStore.updateData {
                    AiringScheduleWidgetData(data = airingSchedulePageModel?.map { it.toData() })
                }
            }
    }
}