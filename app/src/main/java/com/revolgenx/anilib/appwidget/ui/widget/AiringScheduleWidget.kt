package com.revolgenx.anilib.appwidget.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.appwidget.service.AiringScheduleRemoteViewsService
import com.revolgenx.anilib.common.preference.AiringWidgetPreference
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.util.getPendingIntentUpdateFlag
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class AiringScheduleWidget : AppWidgetProvider() {


    companion object {
        const val REFRESH_ACTION = "airing_schedule_refresh_action"
        const val NEXT_PAGE_ACTION = "airing_schedule_next_page"
        const val PREVIOUS_PAGE_ACTION = "airing_schedule_previous_page"
        const val WIDGET_MEDIA_ITEM_ACTION = "airing_schedule_media_item_action"
        const val WIDGET_MEDIA_ITEM = "airing_schedule_media_item"
    }

    private val weekFields by lazy { WeekFields.of(Locale.getDefault()) }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val currentDate = getTodayDate(context)

        appWidgetIds.forEach { id ->
            val remoteViews =
                RemoteViews(context.packageName, R.layout.airing_schedule_widget_layout)

            remoteViews.setTextViewText(R.id.airing_widget_header, currentDate)

            resetToDefaultPage(context, id, remoteViews)
            setOnPendingClicksOnView(context, id, remoteViews)

            val serviceIntent = Intent(context, AiringScheduleRemoteViewsService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            val clickPendingIntent =
                getBroadcastPendingIntent(context, WIDGET_MEDIA_ITEM_ACTION, id)
            remoteViews.setPendingIntentTemplate(R.id.airing_widget_list_view, clickPendingIntent)

            remoteViews.setRemoteAdapter(R.id.airing_widget_list_view, serviceIntent)
            remoteViews.setEmptyView(R.id.airing_widget_list_view, R.id.wg_empty_view)
            appWidgetManager.updateAppWidget(id, remoteViews)

            notifyDataChanged(context, id)
        }
    }


    private fun getTodayDate(context: Context): String {
        val isWeeklyAiring = AiringWidgetPreference.isAiringWeekly(context)

        val startDateTime = if (isWeeklyAiring)
            ZonedDateTime.now().with(weekFields.dayOfWeek(), 1)
                .with(LocalTime.MIN) else ZonedDateTime.now().with(LocalTime.MIN)

        val endDateTime = if (isWeeklyAiring)
            ZonedDateTime.now().with(weekFields.dayOfWeek(), 7)
                .with(LocalTime.MAX) else ZonedDateTime.now().with(LocalTime.MAX)

        val dateFormatPattern = context.getString(R.string.date_format_pattern)
        return if (isWeeklyAiring) {
            context.getString(R.string.day_range_string).format(
                startDateTime.format(
                    DateTimeFormatter.ofPattern(
                        dateFormatPattern
                    )
                ), endDateTime.format(DateTimeFormatter.ofPattern(dateFormatPattern))
            )
        } else {
            startDateTime.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy"))
        }
    }


    private fun resetToDefaultPage(context: Context, id: Int, remoteViews: RemoteViews) {
        AiringWidgetPreference.storePage(context, id, 1)
        remoteViews.setTextViewText(R.id.wg_airing_page_tv, 1.toString())
    }

    private fun setOnPendingClicksOnView(
        context: Context,
        widgetId: Int,
        remoteViews: RemoteViews
    ) {
        remoteViews.setOnClickPendingIntent(
            R.id.airing_schedule_refresh_button,
            getBroadcastPendingIntent(context, REFRESH_ACTION, widgetId)
        )

        remoteViews.setOnClickPendingIntent(
            R.id.wg_airing_next_page,
            getBroadcastPendingIntent(context, NEXT_PAGE_ACTION, widgetId)
        )

        remoteViews.setOnClickPendingIntent(
            R.id.wg_airing_prev_page,
            getBroadcastPendingIntent(context, PREVIOUS_PAGE_ACTION, widgetId)
        )

        remoteViews.setOnClickPendingIntent(
            R.id.airing_schedule_open_button,
            openAiringSchedulePendingIntent(context)
        )

    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == null) return

        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            val widgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val appWidgetManager = context.appWidgetManager()
            when (intent.action) {
                REFRESH_ACTION -> {
                    val currentDate = getTodayDate(context)
                    val remoteViews =
                        RemoteViews(context.packageName, R.layout.airing_schedule_widget_layout)
                    remoteViews.setTextViewText(R.id.airing_widget_header, currentDate)
                    appWidgetManager.partiallyUpdateAppWidget(widgetId, remoteViews)
                    appWidgetManager.notifyAppWidgetViewDataChanged(
                        widgetId,
                        R.id.airing_widget_list_view
                    )
                }
                NEXT_PAGE_ACTION -> {
                    goToPage(context, widgetId, true)
                }
                PREVIOUS_PAGE_ACTION -> {
                    goToPage(context, widgetId, false)
                }
                WIDGET_MEDIA_ITEM_ACTION -> {
                    val mediaId = intent.getIntExtra(WIDGET_MEDIA_ITEM, -1)

                    if (AiringWidgetPreference.clickOpenListEditor(context)) {

                        context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            ).apply {
                                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                this.action = MainActivity.MEDIA_INFO_ACTION_KEY
                                this.putExtra(
                                    MainActivity.MEDIA_INFO_DATA_KEY, MediaInfoMeta(
                                        mediaId,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                    )
                                )
                            })
                    } else {

                        context.startActivity(Intent(context, MainActivity::class.java).apply {
                            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            this.action = MainActivity.ENTRY_LIST_ACTION_KEY
                            this.putExtra(
                                MainActivity.ENTRY_LIST_DATA_KEY, mediaId
                            )
                        })

                    }

                }
            }
        }

    }

    private fun goToPage(context: Context, widgetId: Int, isNext: Boolean) {
        val currentPage = AiringWidgetPreference.getPage(context, widgetId)
        var newPage = if (isNext) currentPage + 1 else currentPage - 1
        if (newPage == 0) {
            newPage = 1
        }
        updatePageView(context, widgetId, newPage)
        AiringWidgetPreference.storePage(context, widgetId, newPage)
        notifyDataChanged(context, widgetId)
    }


    private fun updatePageView(context: Context, widgetId: Int, pageNo: Int) {
        val remoteViews = RemoteViews(context.packageName, R.layout.airing_schedule_widget_layout)
        remoteViews.setTextViewText(R.id.wg_airing_page_tv, pageNo.toString())
        context.appWidgetManager().partiallyUpdateAppWidget(widgetId, remoteViews)
    }

    private fun notifyDataChanged(context: Context, widgetId: Int) {
        context.appWidgetManager()
            .notifyAppWidgetViewDataChanged(widgetId, R.id.airing_widget_list_view)
    }

    private fun Context.appWidgetManager() = AppWidgetManager.getInstance(this)


    private fun getBroadcastPendingIntent(
        context: Context,
        action: String,
        appWidgetId: Int
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context, 0, Intent(context, javaClass).also {
                it.action = action
                it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                it.data = Uri.parse(it.toUri(Intent.URI_INTENT_SCHEME))
            }, getPendingIntentUpdateFlag()
        )
    }


    private fun openAiringSchedulePendingIntent(context: Context): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                action = MainActivity.OPEN_AIRING_ACTION_KEY
            },
            getPendingIntentUpdateFlag()
        )
    }

}