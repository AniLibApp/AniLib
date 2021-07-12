package com.revolgenx.anilib.ui.fragment.airing

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.otaliastudios.elements.extensions.SimplePresenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.infrastructure.event.ListEditorResultEvent
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.AiringListDisplayMode
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.databinding.AiringFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.source.home.airing.AiringHeaderSource
import com.revolgenx.anilib.ui.bottomsheet.airing.CalendarViewBottomSheetDialog
import com.revolgenx.anilib.ui.dialog.AiringFragmentFilterDialog
import com.revolgenx.anilib.ui.presenter.airing.AiringPresenter
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.viewmodel.airing.AiringViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class AiringFragment : BasePresenterFragment<AiringMediaModel>() {
    override val basePresenter: Presenter<AiringMediaModel>
        get() = AiringPresenter(requireContext())
    override val baseSource: Source<AiringMediaModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<AiringViewModel>()

    override fun createSource(): Source<AiringMediaModel> {
        return viewModel.createSource()
    }

    private var _airingBinding: AiringFragmentLayoutBinding? = null
    private val airingBinding: AiringFragmentLayoutBinding get() = _airingBinding!!


    override var selfAddLayoutManager: Boolean = false
    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.airing_menu

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }


    override fun reloadLayoutManager() {
        var span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

        when (getAiringDisplayMode()) {
            AiringListDisplayMode.COMPACT -> {

            }

            AiringListDisplayMode.NORMAL, AiringListDisplayMode.MINIMAL_LIST -> {
                span /= 2
            }
        }

        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItemViewType(position) == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }

        super.reloadLayoutManager()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        _airingBinding = AiringFragmentLayoutBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        airingBinding.airingContainerFrameLayout.addView(view)
        return airingBinding.root
    }


    override fun getBaseToolbar(): Toolbar {
        return airingBinding.airingToolbar.dynamicToolbar
    }

    @SuppressLint("RestrictedApi")
    override fun onToolbarInflated() {
        val menu = getBaseToolbar().menu
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        menu.findItem(R.id.weekly_filter).isChecked = showAiringWeekly(requireContext())
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishActivity()
                true
            }
            R.id.airing_next -> {
                goToNext()
                true
            }

            R.id.airing_previous -> {
                goToPrevious()
                true
            }
            R.id.airing_custom -> {

                openCalendarChooserSheet()
                true
            }
            R.id.airing_filter -> {
                val airingDialog = AiringFragmentFilterDialog.newInstance()
                airingDialog.onDoneListener = {
                    if (context != null) {
                        viewModel.updateField(requireContext())
                        storeAiringField(requireContext(), viewModel.field)
                        createSource()
                        invalidateAdapter()
                    }
                }
                airingDialog.show(
                    childFragmentManager,
                    AiringFragmentFilterDialog::class.java.simpleName
                )
                true
            }
            R.id.weekly_filter -> {
                showAiringWeekly(requireContext(), !item.isChecked)
                item.isChecked = !item.isChecked
                viewModel.updateDateRange(item.isChecked)
                updateToolbarTitle()
                createSource()
                invalidateAdapter()
                true
            }

            R.id.display_modes -> {
                makeArrayPopupMenu(
                    requireView().findViewById(R.id.airing_previous),
                    resources.getStringArray(R.array.airing_display_modes),
                    selectedPosition = getAiringDisplayMode().ordinal
                ) { _, _, index, _ ->
                    setAiringDisplayMode(index)
                    reloadLayoutManager()
                    invalidateAdapter()
                }
                true
            }
            else -> false
        }
    }

    private fun openCalendarChooserSheet() {
        CalendarViewBottomSheetDialog().show(requireContext()) {
            selectionMode = if (viewModel.isDateTypeRange) {
                selectedDateStart = viewModel.startDateTime.toLocalDate()
                selectedDateEnd = viewModel.endDateTime.toLocalDate()
                CalendarViewBottomSheetDialog.SelectionMode.RANGE
            } else {
                selectedDate = viewModel.startDateTime.toLocalDate()
                CalendarViewBottomSheetDialog.SelectionMode.DATE
            }

            listener = { startDate, endDate ->
                if (viewModel.isDateTypeRange) {
                    viewModel.startDateTime =
                        startDate.atStartOfDay(ZoneId.systemDefault()).with(LocalTime.MIN)
                    endDate?.atTime(LocalTime.MAX)!!.with(LocalTime.MAX)
                        .let {
                            viewModel.endDateTime = it.atZone(ZoneId.systemDefault())
                        }
                } else {
                    viewModel.startDateTime =
                        startDate.atStartOfDay(ZoneId.systemDefault()).with(LocalTime.MIN)
                    viewModel.endDateTime =
                        startDate.atStartOfDay(ZoneId.systemDefault()).with(LocalTime.MAX)
                }
                updateToolbarTitle()
                createSource()
                invalidateAdapter()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        airingBinding.airingToolbar.dynamicToolbar.setOnClickListener {
            openCalendarChooserSheet()
        }

        if (requireContext().loggedIn()) {
            with(viewModel.field) {
                userId = requireContext().userId()
            }
        }

        viewModel.updateField(requireContext())
        viewModel.updateDateRange(showAiringWeekly(requireContext()))


        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    goToPrevious();
                } else if (direction == ItemTouchHelper.LEFT) {
                    goToNext()
                }
            }
        }).attachToRecyclerView(binding.basePresenterRecyclerView)
        updateToolbarTitle()
    }

    private fun goToPrevious() {
        viewModel.previous()
        updateToolbarTitle()
        createSource()
        invalidateAdapter()
    }

    private fun goToNext() {
        viewModel.next()
        updateToolbarTitle()
        createSource()
        invalidateAdapter()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onListEditorEvent(event: ListEditorResultEvent) {
        viewModel.field.isNewField = true

        if (!event.listEditorResultMeta.deleted) {
            event.listEditorResultMeta.let {
                viewModel.updateMediaProgress(it.mediaId, it.progress)
            }
        }
        adapter?.notifyDataSetChanged()
    }

    private fun updateToolbarTitle() {
        val startDate = viewModel.startDateTime
        val endDate = viewModel.endDateTime
        val isSingleDateType = !viewModel.isDateTypeRange
        val dayRangeString = if (isSingleDateType) {
            startDate.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
        } else {
            getString(R.string.day_range_string).format(
                startDate.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ),

                endDate.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
            )
        }
        val dateFormatPattern = requireContext().getString(R.string.date_format_pattern)

        val dayDateRange = if (isSingleDateType) {
            startDate.format(DateTimeFormatter.ofPattern(dateFormatPattern))
        } else {
            getString(R.string.day_range_string).format(
                startDate.format(
                    DateTimeFormatter.ofPattern(
                        dateFormatPattern
                    )
                ), endDate.format(DateTimeFormatter.ofPattern(dateFormatPattern))
            )
        }

        getBaseToolbar().title = dayRangeString
        getBaseToolbar().subtitle = dayDateRange
    }

    override fun adapterBuilder(): Adapter.Builder {
        val builder = super.adapterBuilder()

        if (viewModel.isDateTypeRange) {
            builder.addSource(AiringHeaderSource())
            builder.addPresenter(
                SimplePresenter<HeaderSource.Data<AiringMediaModel, String>>(
                    requireContext(),
                    R.layout.header_presenter_layout,
                    HeaderSource.ELEMENT_TYPE
                ) { v, header ->
                    (v as TextView).text = header.header
                })
        }

        return builder
    }

    override fun onDestroyView() {
        _airingBinding = null
        super.onDestroyView()
    }

}
