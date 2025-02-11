package com.revolgenx.anilib.widget.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revolgenx.anilib.airing.data.field.AiringScheduleField
import com.revolgenx.anilib.airing.ui.model.AiringScheduleModel
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.type.AiringSort
import com.revolgenx.anilib.widget.data.service.AiringScheduleWidgetService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZonedDateTime

class AiringScheduleWidgetResource(
    private val airingScheduleWidgetService: AiringScheduleWidgetService,
    private val appPreferencesDataStore: AppPreferencesDataStore
) {
    val resource: MutableState<ResourceState<List<AiringScheduleModel>>?> = mutableStateOf(null)

    var startDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MIN))
    var endDateTime by mutableStateOf(ZonedDateTime.now().with(LocalTime.MAX))


    private val field: AiringScheduleField = with(appPreferencesDataStore) {
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

    private fun updateLocalDateTime() {
        startDateTime = ZonedDateTime.now().with(LocalTime.MIN)
        endDateTime = ZonedDateTime.now().with(LocalTime.MAX)
    }

    suspend fun getScheduleData() {
        airingScheduleWidgetService.getAiringScheduleData().collect {
            it?.let {
                resource.value = ResourceState.success(it)
                updateLocalDateTime()
            }
        }
    }

    suspend fun watchSettings() {
        coroutineScope {
            with(appPreferencesDataStore) {
                launch {
                    widgetAiringSort.collect {
                        val airingSort = AiringSort.entries[it!!]
                        if (field.sort != airingSort) {
                            field.sort = airingSort
                            refresh()
                        }
                    }
                }

                launch {
                    widgetOnlyPlanning.collect {
                        if (field.showOnlyPlanning != it) {
                            field.needMediaListData = needMediaListData(
                                showOnlyWatching = field.showOnlyWatching,
                                showOnlyPlanning = it!!
                            )
                            field.showOnlyPlanning = it
                            refresh()
                        }
                    }
                }

                launch {
                    widgetOnlyWatching.collect {
                        if (field.showOnlyWatching != it) {
                            field.needMediaListData = needMediaListData(
                                showOnlyWatching = it!!,
                                showOnlyPlanning = field.showOnlyPlanning
                            )
                            field.showOnlyWatching = it
                            refresh()
                        }
                    }
                }

                launch {
                    widgetIncludeAlreadyAired.collect {
                        if (field.notYetAired != !it!!) {
                            field.notYetAired = !it
                            refresh()
                        }
                    }
                }
            }
        }
    }

    suspend fun refresh() {
        val storedAiringSchedule = airingScheduleWidgetService.getAiringScheduleData().get()
        resource.value = ResourceState.loading(data = storedAiringSchedule)
        airingScheduleWidgetService.getAiringSchedule()
            .onEach {
                resource.value = ResourceState.success(it)
            }.catch {
                resource.value = ResourceState.error(it.message, storedAiringSchedule)
            }.collect()
    }


    private fun needMediaListData(showOnlyWatching: Boolean, showOnlyPlanning: Boolean) =
        ((field.showOnlyPlanning || field.showOnlyWatching)
                && (field.showOnlyWatching != showOnlyWatching
                || field.showOnlyPlanning != showOnlyPlanning)
                )


    suspend fun removeAiringScheduleData(){
        airingScheduleWidgetService.removeAiringScheduleData()
    }
}