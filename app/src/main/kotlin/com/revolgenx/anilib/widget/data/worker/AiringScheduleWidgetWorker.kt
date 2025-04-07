package com.revolgenx.anilib.widget.data.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.revolgenx.anilib.widget.data.service.AiringScheduleWidgetService
import com.revolgenx.anilib.widget.ui.AiringScheduleWidget
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class AiringScheduleWidgetWorker(
    private val context: Context,
    params: WorkerParameters,
    private val airingScheduleWidgetService: AiringScheduleWidgetService
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        try {
            airingScheduleWidgetService.getAiringSchedule().collect()
            AiringScheduleWidget().updateAll(context = context)
        } catch (ex: Exception) {
            Timber.e(ex, "Failed to doWork in WidgetWorker")
        }
        return Result.success()
    }
}