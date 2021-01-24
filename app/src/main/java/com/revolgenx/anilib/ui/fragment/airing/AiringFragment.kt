package com.revolgenx.anilib.ui.fragment.airing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.preference.storeDiscoverAiringField
import com.revolgenx.anilib.infrastructure.event.ListEditorResultEvent
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.AiringFilterMeta
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.databinding.AiringFragmentLayoutBinding
import com.revolgenx.anilib.ui.bottomsheet.airing.CalendarViewBottomSheetDialog
import com.revolgenx.anilib.ui.dialog.AiringFragmentFilterDialog
import com.revolgenx.anilib.ui.presenter.airing.AiringPresenter
import com.revolgenx.anilib.ui.viewmodel.airing.AiringViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalTime
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

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
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
        with(activity as ContainerActivity) {
            setSupportActionBar(airingBinding.airingToolbar.dynamicToolbar)
            this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        airingBinding.airingContainerFrameLayout.addView(view)
        return airingBinding.root
    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.airing_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        menu.findItem(R.id.weekly_filter).isChecked = showAiringWeekly(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
                airingDialog.onDoneListener = { meta ->
                    viewModel.field.let {
                        it.notYetAired = meta.notYetAired
                        it.showFromWatching = meta.showFromWatching
                        it.showFromPlanning = meta.showFromPlanning
                        it.sort = meta.sort
                    }
                    storeAiringField(requireContext(), viewModel.field)
                    createSource()
                    invalidateAdapter()
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
            else -> super.onOptionsItemSelected(item)
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
                viewModel.startDateTime = startDate.atStartOfDay(ZoneOffset.UTC).with(LocalTime.MIN)
                viewModel.endDateTime =
                    endDate?.atTime(LocalTime.MAX)?.atZone(ZoneOffset.UTC)?.with(LocalTime.MAX)
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


        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
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

    private fun goToPrevious(){
        viewModel.previous()
        updateToolbarTitle()
        createSource()
        invalidateAdapter()
    }

    private fun goToNext(){
        viewModel.next()
        updateToolbarTitle()
        createSource()
        invalidateAdapter()
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListEditorEvent(event: ListEditorResultEvent) {
        event.listEditorResultMeta.let {
            viewModel.updateMediaProgress(it.mediaId, it.progress)
        }
        adapter?.notifyDataSetChanged()
        EventBus.getDefault().removeStickyEvent(event)
    }

    private fun updateToolbarTitle() {
        (activity as? ContainerActivity)?.let {
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

            val dayDateRange = if (isSingleDateType) {
                startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            } else {
                getString(R.string.day_range_string).format(
                    startDate.format(
                        DateTimeFormatter.ofPattern(
                            "MM/dd/yyyy"
                        )
                    ), endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                )
            }

            it.supportActionBar?.title = dayRangeString
            it.supportActionBar?.subtitle = dayDateRange
        }
    }

    override fun onDestroyView() {
        _airingBinding = null
        super.onDestroyView()
    }

}
