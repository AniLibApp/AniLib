package com.revolgenx.anilib.appwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.revolgenx.anilib.R
import com.revolgenx.anilib.appwidget.service.AiringScheduleRemoteViewsService
import com.revolgenx.anilib.appwidget.infrastructure.event.AiringScheduleWidgetEvent
import com.revolgenx.anilib.common.preference.WidgetPreference
import com.revolgenx.anilib.ui.view.makeToast
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AiringScheduleWidget : AppWidgetProvider() {


    companion object {
        const val REFRESH_ACTION = "airing_schedule_refresh_action"
        const val NEXT_PAGE_ACTION = "airing_schedule_next_page"
        const val PREVIOUS_PAGE_ACTION = "airing_schedule_previous_page"
    }

    private val startDateTime get() = ZonedDateTime.now().with(LocalTime.MIN)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val currentDate = startDateTime.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy"))
        appWidgetIds.forEach { id ->
            val remoteViews =
                RemoteViews(context.packageName, R.layout.airing_schedule_widget_layout)
            remoteViews.setOnClickPendingIntent(
                R.id.airing_schedule_refresh_button,
                getPendingIntent(context, REFRESH_ACTION, id)
            )


            remoteViews.setTextViewText(R.id.airing_widget_header, currentDate)

            val serviceIntent = Intent(context, AiringScheduleRemoteViewsService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))
            remoteViews.setRemoteAdapter(R.id.airing_widget_list_view, serviceIntent)
            appWidgetManager.updateAppWidget(id, remoteViews)
            appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.airing_widget_list_view)
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == null) return

        when (intent.action) {
            REFRESH_ACTION   -> {
                val widgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                )
                context.sendBroadcast(Intent(REFRESH_ACTION))
            }
        }

        context.makeToast(null, "tosating")
    }

    private fun sendRefreshEvent(widgetId: Int) {
        AiringScheduleWidgetEvent.RefreshScheduleEvent(widgetId).postEvent
    }


    private fun getPendingIntent(
        context: Context,
        action: String,
        appWidgetId: Int
    ): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(context,javaClass).also {
            it.action =action
            it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            it.data = Uri.parse(it.toUri(Intent.URI_INTENT_SCHEME))
        }, PendingIntent.FLAG_UPDATE_CURRENT)
    }

}