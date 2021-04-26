package com.revolgenx.anilib.appwidget.ui.fragment

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.appwidget.ui.widget.AiringScheduleWidget
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.databinding.AiringWidgetConfigFragmentLayoutBinding
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeToast
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class AiringWidgetConfigFragment : BaseToolbarFragment<AiringWidgetConfigFragmentLayoutBinding>() {

    private val field by lazy {
        getAiringScheduleFieldForWidget(requireContext())
    }

    override var setHomeAsUp: Boolean = true
    override var titleRes: Int? = R.string.airing_widget_config
    override val noScrollToolBar: Boolean = true
    
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AiringWidgetConfigFragmentLayoutBinding {
        return AiringWidgetConfigFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {

            if (!requireContext().loggedIn()) {
                wgShowFromPlanning.visibility = View.GONE
                wgShowFromWatching.visibility = View.GONE
            }

            wgIncludeAlreadyAired.isChecked = !field.notYetAired
            wgIsAiringWeekly.isChecked = field.isWeeklyTypeDate
            wgShowFromPlanning.isChecked = field.showFromPlanning
            wgShowFromWatching.isChecked = field.showFromWatching
            wgClickOpenListEditor.isChecked = AiringWidgetPreference.clickOpenListEditor(requireContext())


            val canShowSpinnerIcon = getApplicationLocale() == "de"
            wgAiringSort.adapter =
                makeSpinnerAdapter(
                    requireContext(),
                    resources.getStringArray(R.array.airing_sort).mapIndexed { index, s ->
                        var icon: Drawable? = null
                        if(canShowSpinnerIcon) {
                            icon = if (index % 2 == 0) {
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_asc)
                            } else {
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_desc)
                            }
                        }
                        DynamicSpinnerItem(icon, s)
                    }
                )
            wgAiringSort.setSelection(field.sort!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        field.updateField()
        super.onSaveInstanceState(outState)
    }

    private fun AiringMediaField.updateField() {
        notYetAired = !binding.wgIncludeAlreadyAired.isChecked
        isWeeklyTypeDate = binding.wgIsAiringWeekly.isChecked
        showFromPlanning = binding.wgShowFromPlanning.isChecked
        showFromWatching = binding.wgShowFromWatching.isChecked
        sort = binding.wgAiringSort.selectedItemPosition
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_menu -> {
                val oldIsWeekly = field.isWeeklyTypeDate
                field.updateField()

                val isWeeklyAiring = field.isWeeklyTypeDate
                val appWidgetManager = AppWidgetManager.getInstance(requireContext())
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(
                        requireContext(),
                        AiringScheduleWidget::class.java
                    )
                )

                val remoteViews =
                    RemoteViews(
                        requireContext().packageName,
                        R.layout.airing_schedule_widget_layout
                    )

                if (oldIsWeekly != isWeeklyAiring) {
                    val weekFields = WeekFields.of(Locale.getDefault())
                    val startDateTime = if (isWeeklyAiring)
                        ZonedDateTime.now().with(weekFields.dayOfWeek(), 1)
                            .with(LocalTime.MIN) else ZonedDateTime.now().with(LocalTime.MIN)

                    val endDateTime = if (isWeeklyAiring)
                        ZonedDateTime.now().with(weekFields.dayOfWeek(), 7)
                            .with(LocalTime.MAX) else ZonedDateTime.now().with(LocalTime.MAX)
                    val dateFormatPattern = requireContext().getString(R.string.date_format_pattern)

                    val currentDate = if (isWeeklyAiring) {
                        requireContext().getString(R.string.day_range_string).format(
                            startDateTime.format(
                                DateTimeFormatter.ofPattern(
                                    dateFormatPattern
                                )
                            ), endDateTime.format(DateTimeFormatter.ofPattern(dateFormatPattern))
                        )
                    } else {
                        startDateTime.format(DateTimeFormatter.ofPattern("EE, dd MMM, yyyy"))
                    }


                    appWidgetIds.forEach {
                        resetToDefaultPage(requireContext(), it, remoteViews)
                    }
                    remoteViews.setTextViewText(R.id.airing_widget_header, currentDate)
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, remoteViews)

                }

                appWidgetManager.notifyAppWidgetViewDataChanged(
                    appWidgetIds,
                    R.id.airing_widget_list_view
                )

                storeAiringScheduleFieldForWidget(requireContext(), field)
                AiringWidgetPreference.clickOpenListEditor(requireContext(), binding.wgClickOpenListEditor.isChecked)
                makeToast(R.string.saved_successfully)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun resetToDefaultPage(context: Context, id: Int, remoteViews: RemoteViews) {
        AiringWidgetPreference.storePage(context, id, 1)
        remoteViews.setTextViewText(R.id.wg_airing_page_tv, 1.toString())
    }
}