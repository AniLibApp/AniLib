package com.revolgenx.anilib.widget.ui

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.revolgenx.anilib.widget.data.worker.AiringScheduleWidgetWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.concurrent.TimeUnit

class AiringScheduleWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    private val airingWidgetResource: AiringScheduleWidgetResource = get()
    override val glanceAppWidget: GlanceAppWidget = AiringScheduleWidget()
    companion object {
        private const val WIDGET_WORK_NAME = "AiringScheduleWidgetWorker"
        private var widgetScope: CoroutineScope? = null
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleAiringScheduleWorker(context)
        widgetScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        widgetScope!!.launch {
            airingWidgetResource.refresh()
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WorkManager.getInstance(context).cancelUniqueWork(WIDGET_WORK_NAME)
        runBlocking {
            airingWidgetResource.removeAiringScheduleData()
        }
        widgetScope?.cancel()
        widgetScope = null
    }

    private fun scheduleAiringScheduleWorker(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<AiringScheduleWidgetWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setInitialDelay(30, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WIDGET_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
}