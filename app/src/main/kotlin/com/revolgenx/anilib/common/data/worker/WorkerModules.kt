package com.revolgenx.anilib.common.data.worker

import com.revolgenx.anilib.notification.data.worker.NotificationWorker
import com.revolgenx.anilib.widget.data.worker.AiringScheduleWidgetWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val workerModules = module {
    workerOf(::NotificationWorker)
    workerOf(::AiringScheduleWidgetWorker)
}