package com.revolgenx.anilib.appwidget.service

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.net.toUri
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.datasource.DataSources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.CloseableStaticBitmap
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.revolgenx.anilib.R
import com.revolgenx.anilib.appwidget.infrastructure.event.AiringScheduleWidgetEvent
import com.revolgenx.anilib.appwidget.widget.AiringScheduleWidget
import com.revolgenx.anilib.common.preference.getAiringScheduleFieldForWidget
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.airing.AiringMediaService
import com.revolgenx.anilib.util.EventBusListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.lang.ref.WeakReference
import java.time.LocalTime
import java.time.ZonedDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AiringScheduleRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return AiringScheduleWidgetFactory(applicationContext, intent)
    }


    class AiringScheduleWidgetFactory(
        private val context: Context, private val intent: Intent
    ) : RemoteViewsFactory,KoinComponent {

        private val appWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )

        private val items = mutableListOf<AiringMediaModel>()

        private val airingMediaService: AiringMediaService by inject()
        private val compositeDisposable = CompositeDisposable()
        private var field: AiringMediaField = AiringMediaField()

        private val startDateTime get() = ZonedDateTime.now().with(LocalTime.MIN)
        private val endDateTime get() = ZonedDateTime.now().with(LocalTime.MAX)

        private val appWidgetManager = AppWidgetManager.getInstance(context)

        private val closeableReferences =
            mutableMapOf<String, WeakReference<DataSource<CloseableReference<CloseableImage>>>>()

        private val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when(intent.action){
                    AiringScheduleWidget.REFRESH_ACTION->{
                        Toast.makeText(context, "hello", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        override fun onCreate() {
            context.registerReceiver(receiver, IntentFilter().also {
                it.addAction(AiringScheduleWidget.REFRESH_ACTION)
                it.addAction(AiringScheduleWidget.NEXT_PAGE_ACTION)
                it.addAction(AiringScheduleWidget.PREVIOUS_PAGE_ACTION)
            })

            field.perPage = 8
            if (context.loggedIn()) {
                field.userId = context.userId()
            }
            updateField()
        }

        private fun updateField() {
            getAiringScheduleFieldForWidget(context, field)

            field.airingGreaterThan = startDateTime.toEpochSecond().toInt()
            field.airingLessThan = endDateTime.toEpochSecond().toInt()
        }

        private fun nextPage() {
            field.page = field.page!! + 1
        }

        private fun previousPage() {
            val newPage = field.page!! - 1
            if (newPage != 0) {
                field.page = newPage
            }
        }

        private fun resetPage() {
            field.page = 1
        }

        override fun onDataSetChanged() {
            runBlocking {
                showLoader(true)
                field.notYetAired = false
                val suspendedQuery =
                    suspendCoroutine<Resource<List<AiringMediaModel>>> { continuation ->
                        airingMediaService.getAiringMedia(field, compositeDisposable) {
                            continuation.resume(it)
                        }
                    }
                if (suspendedQuery.status == Status.SUCCESS && suspendedQuery.data != null) {
                    val airingModels = suspendedQuery.data
                    items.clear()
                    items.addAll(airingModels)
                } else {
                    Timber.e(suspendedQuery.exception, suspendedQuery.message)
                }
                showLoader(false)
            }
        }

        @Subscribe
        fun onEvent(event: AiringScheduleWidgetEvent) {
            if (event.widgetId != appWidgetId) return

            when (event) {
                is AiringScheduleWidgetEvent.RefreshScheduleEvent -> {
                    resetPage()
                    notifyDataChanged()
                }
                is AiringScheduleWidgetEvent.ChangePageEvent -> {
                    if (event.isNextPage) {
                        nextPage()
                    } else {
                        previousPage()
                    }
                    notifyDataChanged()
                }
                is AiringScheduleWidgetEvent.FieldChangeEvent -> {
                    updateField()
                    notifyDataChanged()
                }
            }
        }

        private fun notifyDataChanged() {
            appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.id.airing_widget_list_view
            )
        }

        override fun getCount(): Int {
            return items.size
        }

        override fun getViewAt(position: Int): RemoteViews {

            val item = items[position]

            val airingTimeModel = item.airingTimeModel!!
            val airingAtTime = airingTimeModel.airingAt!!.airingTime
            val airingEpisode = airingTimeModel.episode
            val timeUntilAiring = airingTimeModel.timeUntilAiring!!
            val coverImage = item.coverImage?.sImage

            val remoteViews =
                RemoteViews(context.packageName, R.layout.airing_schedule_widget_item_layout)
            remoteViews.setTextViewText(
                R.id.wg_media_name_tv,
                items[position].title!!.title(context)
            )
            remoteViews.setTextViewText(
                R.id.wg_airing_at_tv,
                context.getString(
                    R.string.widget_airing_at_format,
                    airingEpisode,
                    airingAtTime,
                    timeUntilAiring.hour,
                    timeUntilAiring.min,
                    timeUntilAiring.sec
                )
            )

            val dataSource = Fresco.getImagePipeline().fetchDecodedImage(
                ImageRequestBuilder.newBuilderWithSource(coverImage!!.toUri())
                    .setImageDecodeOptions(
                        ImageDecodeOptions.newBuilder()
                            .setForceStaticImage(true).build()
                    )
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build(),
                context
            )
            val result =
                DataSources.waitForFinalResult<CloseableReference<CloseableImage>>(
                    dataSource
                )
            if (result != null) {
                (result.get() as CloseableStaticBitmap).underlyingBitmap.copy(
                    (result.get() as CloseableStaticBitmap).underlyingBitmap.config,
                    true
                ).let {
                    remoteViews.setImageViewBitmap(R.id.wg_airing_media_iv, it);
                }
            }
            return remoteViews
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }


        private fun showLoader(b: Boolean) {
            val widgetRemoteView =
                RemoteViews(context.packageName, R.layout.airing_schedule_widget_layout)
            if (b) {
                widgetRemoteView.setViewVisibility(R.id.wg_loading_bar, View.VISIBLE)
            } else {
                widgetRemoteView.setViewVisibility(R.id.wg_loading_bar, View.GONE)
            }
            appWidgetManager.updateAppWidget(appWidgetId, widgetRemoteView)
        }

        override fun onDestroy() {
            context.unregisterReceiver(receiver)
            closeableReferences.clear()
            compositeDisposable.clear()
        }

    }
}