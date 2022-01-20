package com.revolgenx.anilib.ui.bottomsheet.calendar

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.contrastPrimaryTextColorWithAccent
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.databinding.CalendarDayItemBinding
import com.revolgenx.anilib.databinding.CalendarHeaderItemBinding
import com.revolgenx.anilib.databinding.CalendarViewBottomSheetLayoutBinding
import com.revolgenx.anilib.ui.bottomsheet.calendar.adapter.MonthAdapter
import com.revolgenx.anilib.ui.bottomsheet.calendar.adapter.YearAdapter
import com.revolgenx.anilib.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.ui.view.animValues
import com.revolgenx.anilib.ui.view.fadeIn
import com.revolgenx.anilib.util.dp
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit

class CalendarViewBottomSheetDialog :
    DynamicBottomSheetFragment<CalendarViewBottomSheetLayoutBinding>() {


    private lateinit var monthAdapter: MonthAdapter
    private lateinit var monthLayoutManger: GridLayoutManager

    private lateinit var yearAdapter: YearAdapter
    private lateinit var yearLayoutManger: LinearLayoutManager

    override val positiveText: Int = R.string.done
    override val negativeText: Int = R.string.cancel

    override val title: Int?
        get() = if (selectionMode == SelectionMode.RANGE) R.string.select_days_within_a_week else R.string.select_date

    private val fullDate = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val dateRangeStartNoMonth = DateTimeFormatter.ofPattern("d")
    private val dateRangeStart = DateTimeFormatter.ofPattern("d MMM")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

    private var colorTextActive: Int = contrastAccentWithBg
    private var colorText: Int = dynamicTextColorPrimary
    private var iconColor: Int = dynamicTextColorPrimary
    private var colorTextInverse: Int = contrastPrimaryTextColorWithAccent
    private var highlightColor: Int = DynamicColorUtils.setAlpha(contrastAccentWithBg, 15)


    private lateinit var selectionShapeStart: InsetDrawable
    private lateinit var selectionShapeEnd: InsetDrawable

    private lateinit var selectionShapeStartBg: InsetDrawable
    private lateinit var selectionShapeEndBg: InsetDrawable

    private lateinit var selectionShapeEndLayer: LayerDrawable

    private lateinit var selectionShapeMiddle: InsetDrawable
    private lateinit var dayTodayDrawable: InsetDrawable
    private lateinit var disabledDayDrawable: InsetDrawable

    private var drawableAnimator: ValueAnimator? = null

    private val today = LocalDate.now()
    private var calendarViewActive: Boolean = true

    private var calendarMode: CalendarMode = CalendarMode.MONTH

    private var maxRange: Int = 7

    private var disabledDates: MutableList<Calendar> = mutableListOf()

    private var disableTimeLine: TimeLine? = null

    private val disablePast: Boolean
        get() = disableTimeLine == TimeLine.PAST

    private val disableFuture: Boolean
        get() = disableTimeLine == TimeLine.FUTURE

    private var rangeYears: Int = 100
    private var selectedViewDate: LocalDate = LocalDate.now()

    private var displayButtons = true


    var selectionMode: SelectionMode = SelectionMode.RANGE
    var selectedDate: LocalDate? = null
    var selectedDateStart: LocalDate? = null
    var selectedDateEnd: LocalDate? = null
    var listener: ((dateStart: LocalDate, dateEnd: LocalDate?) -> Unit)? = null

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): CalendarViewBottomSheetLayoutBinding {
        return CalendarViewBottomSheetLayoutBinding.inflate(
            inflater,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validate()
        initResources()
        with(binding) {
            setupSwitchViews()
            setupMonths()
            setupYears()
            setupCalendarView()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        onPositiveClicked = {
            save()
        }

        onNegativeClicked = {
            drawableAnimator?.cancel()
            dismiss()
        }

    }

    private fun initResources() {
        val shapeModelRound = ShapeAppearanceModel().toBuilder().apply {
            setAllCorners(CornerFamily.ROUNDED, dp(45f).toFloat())
        }.build()

        /*
            Create InsetDrawables to create a padding around the
            actual drawable within the day item view.
         */

        val insetDp = dp(8f)
        selectionShapeStart = InsetDrawable(MaterialShapeDrawable(shapeModelRound).apply {
            fillColor = ColorStateList.valueOf(colorTextActive)
        }, insetDp, insetDp, insetDp, insetDp)

        selectionShapeEnd = InsetDrawable(MaterialShapeDrawable(shapeModelRound).apply {
            fillColor = ColorStateList.valueOf(colorTextActive)
        }, insetDp, insetDp, insetDp, insetDp)

        selectionShapeEndBg =
            InsetDrawable(MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply {
            }.build()).apply {
                fillColor = ColorStateList.valueOf(highlightColor)
            }, dp(-16f), 0, dp(16f), 0)

        selectionShapeEndLayer =
            LayerDrawable(arrayOf(selectionShapeEnd, selectionShapeEndBg))

        selectionShapeStartBg =
            InsetDrawable(MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply {
            }.build()).apply {
                alpha = 90
                fillColor = ColorStateList.valueOf(highlightColor)
            }, dp(16f), 0, dp(-8f), 0)

        selectionShapeMiddle = InsetDrawable(
            MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().build()).apply {
                fillColor = ColorStateList.valueOf(highlightColor)
            }, dp(0f), dp(8f), dp(0f), dp(8f)
        )

        dayTodayDrawable = InsetDrawable(
            MaterialShapeDrawable(shapeModelRound).apply {
                fillColor = ColorStateList.valueOf(highlightColor)
            },
            insetDp,
            insetDp,
            insetDp,
            insetDp
        )
    }


    private fun CalendarViewBottomSheetLayoutBinding.setupSwitchViews() {

        monthSpinner.buttonTintList = ColorStateList.valueOf(iconColor)
        yearSpinner.buttonTintList = ColorStateList.valueOf(iconColor)
        dateSelected.setTextColor(colorText)

        // With a click on the date, the user will always switch back to the normal calendar view
        dateSelected.setOnClickListener {
            if (calendarViewActive) return@setOnClickListener // Ignore if currently in calendar view
            yearSpinner.apply { if (isChecked) isChecked = false }
            monthSpinner.apply { if (isChecked) isChecked = false }
            switchToCalendarView()
        }

        monthContainer.setOnClickListener {
            monthSpinner.apply { isChecked = !isChecked; if (isChecked) switchToMonthsView() }
            yearSpinner.apply { if (isChecked) isChecked = false }
            if (!monthSpinner.isChecked && !yearSpinner.isChecked)
                switchToCalendarView()
        }

        yearContainer.setOnClickListener {
            yearSpinner.apply { isChecked = !isChecked; if (isChecked) switchToYearsView() }
            monthSpinner.apply { if (isChecked) isChecked = false }
            if (!monthSpinner.isChecked && !yearSpinner.isChecked)
                switchToCalendarView()
        }

    }


    private fun CalendarViewBottomSheetLayoutBinding.setupMonths() {

        monthsRecyclerView.setHasFixedSize(false)
        monthsRecyclerView.setItemViewCacheSize(12)

        monthAdapter = MonthAdapter(
            ctx = requireActivity(),
            disablePast = disablePast,
            disableFuture = disableFuture,
            listener = ::selectMonth
        ).apply {
            updateCurrentYearMonth(selectedViewDate.yearMonth)
        }
        monthsRecyclerView.adapter = monthAdapter

        monthLayoutManger = GridLayoutManager(requireContext(), 2)
        monthsRecyclerView.layoutManager = monthLayoutManger

        monthsRecyclerView.post {

            if (calendarMode == CalendarMode.MONTH) {
                val height = monthsRecyclerView.measuredHeight.minus(dp(32f)).div(6).toInt()
                monthAdapter.updateItemHeight(height)
            }

            monthLayoutManger.scrollToPositionWithOffset(
                YearMonth.now().monthValue,
                monthsRecyclerView.height.div(2)
            )
        }
    }


    private fun CalendarViewBottomSheetLayoutBinding.setupYears() {

        val years = getYears()

        yearAdapter = YearAdapter(requireContext(), years, ::selectYear)
        yearsRecyclerView.adapter = yearAdapter

        yearLayoutManger = LinearLayoutManager(requireContext())
        yearsRecyclerView.layoutManager = yearLayoutManger
    }

    private fun CalendarViewBottomSheetLayoutBinding.setupCalendarView() {

        when (selectionMode) {
            SelectionMode.DATE -> setCurrentDateText(selectedDate)
            SelectionMode.RANGE -> setCurrentDateRangeText(selectedDateStart, selectedDateEnd)
        }

        when (calendarMode) {

            CalendarMode.WEEK_1, CalendarMode.WEEK_2, CalendarMode.WEEK_3 ->
                calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.ALL_MONTHS,
                    outDateStyle = OutDateStyle.END_OF_ROW,
                    maxRowCount = calendarMode.rows,
                    hasBoundaries = false
                )

            CalendarMode.MONTH ->
                calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.ALL_MONTHS,
                    outDateStyle = OutDateStyle.END_OF_ROW,
                    maxRowCount = 6,
                    hasBoundaries = true
                )
        }

        val now: YearMonth = YearMonth.now()
        val start = if (disablePast) now else now.minusYears(rangeYears.toLong())
        val end = if (disableFuture) now else now.plusYears(rangeYears.toLong())

        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarView.scrollMode = ScrollMode.PAGED
        calendarView.setup(start, end, firstDayOfWeek)
        calendarView.scrollToDate(today)
        updateSpinnerValues()

        setupDayBinding()
        setupMonthHeaderBinding()

        setupMonthScrollListener()
    }


    private fun setupMonthScrollListener() {

        binding.calendarView.monthScrollListener = { month ->
            val day = month.weekDays.first().first { it.owner == DayOwner.THIS_MONTH }
            selectedViewDate = day.date
            monthAdapter.updateCurrentYearMonth(day.date.yearMonth)
            updateSpinnerValues()
        }
    }

    private fun setupDayBinding() {

        binding.calendarView.dayBinder = object : DayBinder<DayViewHolder> {
            override fun create(view: View) = DayViewHolder(view, ::onClickDay)
            override fun bind(container: DayViewHolder, day: CalendarDay) {
                container.day = day
                container.binding.day.text = day.day.toString()
                setupDayDesign(container, day)
            }
        }
    }

    private fun onClickDay(day: CalendarDay) {

        if (day.owner != DayOwner.THIS_MONTH) {
            // We don't want to
            return
        }

        when (selectionMode) {

            SelectionMode.DATE -> {
                if (!isDateDisabled(day)) {
                    selectedDate = day.date
                    selectedViewDate = day.date
                    setCurrentDateText(day.date)
                }
            }

            SelectionMode.RANGE -> {
                if (!isDateDisabled(day)) {
                    if (selectedDateStart != null) {
                        if (day.date < selectedDateStart || selectedDateEnd != null) {
                            selectedDateStart = day.date
                            selectedViewDate = day.date
                            selectedDateEnd = null
                        } else if (day.date != selectedDateStart) {
                            if (!containsSelectionDisabledDays(selectedDateStart!!, day.date)) {
                                selectedDateEnd = day.date
                                selectedViewDate = day.date
                            } else {
                                selectedDateStart = day.date
                                selectedViewDate = day.date
                            }
                        }
                    } else {
                        selectedDateStart = day.date
                        selectedViewDate = day.date
                    }
                    setCurrentDateRangeText(selectedDateStart, selectedDateEnd)
                }
            }
        }

        updateSpinnerValues()
        validate() // Validate selection
        binding.calendarView.notifyCalendarChanged() // Update calendar view
    }

    private fun containsSelectionDisabledDays(dateStart: LocalDate, dateEnd: LocalDate): Boolean =
        disabledDates.any { disabledDate ->

            val afterStart = dateStart.dayOfMonth <= disabledDate[Calendar.DAY_OF_MONTH]
                    && dateStart.year <= disabledDate[Calendar.YEAR]
                    && dateStart.month.ordinal <= disabledDate[Calendar.MONTH]

            val afterEnd = dateEnd.dayOfMonth >= disabledDate[Calendar.DAY_OF_MONTH]
                    && dateEnd.year >= disabledDate[Calendar.YEAR]
                    && dateEnd.month.ordinal >= disabledDate[Calendar.MONTH]

            afterStart && afterEnd
        }


    private fun setupDayDesign(container: DayViewHolder, day: CalendarDay) {

        // Hide days from another month
        if (day.owner != DayOwner.THIS_MONTH) {
            container.binding.shape.background = null
            container.binding.day.alpha = 0f
            return
        }

        // Disable day view
        if (isDateDisabled(day)) {
            container.binding.day.alpha = 0.25f
            container.binding.day.setTextColor(colorText)
            return
        }

        container.binding.day.alpha = 1f

        when {

            // Single date or range start day view
            selectedDate == day.date || selectedDateStart == day.date && selectedDateEnd == null -> {
                if (container.binding.shape.background != selectionShapeStart) {
                    container.binding.shape.alpha = 0f
                    container.binding.shape.background =
                        selectionShapeStart
                    container.binding.shape.fadeIn()
                    container.binding.day.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    container.binding.day.setTextColor(colorTextInverse)
                }
            }

            // Range start with bg transition day view
            day.date == selectedDateStart -> {
                drawableAnimator = animValues(0f, 255f, 300, {
                    container.binding.shape.background =
                        getSelectionShapeStartTransitionDrawable(it)
                }, {
                    if (context == null) return@animValues
                    container.binding.shape.fadeIn()
                    container.binding.day.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    container.binding.day.setTextColor(colorTextInverse)
                })
            }

            // Range middle day view
            selectedDateStart != null && selectedDateEnd != null && (day.date > selectedDateStart && day.date < selectedDateEnd) -> {
                if (container.binding.shape.background != selectionShapeMiddle) {
                    container.binding.shape.alpha = 0f
                    container.binding.shape.background =
                        selectionShapeMiddle
                    container.binding.shape.fadeIn()
                }
            }

            // Range end day view
            day.date == selectedDateEnd -> {
                if (container.binding.shape.background != selectionShapeEndLayer) {
                    container.binding.shape.alpha = 0f
                    container.binding.shape.background =
                        selectionShapeEndLayer
                    container.binding.shape.fadeIn()
                    container.binding.day.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    container.binding.day.setTextColor(colorTextInverse)
                }
            }

            // Today day view
            day.date == today -> {
                container.binding.shape.background = dayTodayDrawable
                container.binding.day.setTextAppearance(
                    requireContext(),
                    R.style.TextAppearance_MaterialComponents_Subtitle2
                )
                container.binding.day.setTextColor(colorTextActive)
            }

            // Normal day view
            else -> {
                container.binding.shape.background = null
                container.binding.day.setTextAppearance(
                    requireContext(),
                    R.style.TextAppearance_MaterialComponents_Body2
                )
                container.binding.day.setTextColor(colorText)
            }
        }
    }

    private fun isDateDisabled(day: CalendarDay): Boolean {
        return (disabledDates.any {
            day.date.dayOfMonth == it[Calendar.DAY_OF_MONTH]
                    && day.date.year == it[Calendar.YEAR]
                    && day.date.month.ordinal == it[Calendar.MONTH]
        } || disablePast && day.date.isBefore(today) || disableFuture && day.date.isAfter(today))
    }

    private fun setupMonthHeaderBinding() {
        val daysOfWeek = getDaysOfWeek()
        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewHolder> {
            override fun create(view: View) = MonthViewHolder(CalendarHeaderItemBinding.bind(view))
            override fun bind(holder: MonthViewHolder, month: CalendarMonth) {
                if (holder.binding.legend.tag != null) return
                holder.binding.legend.tag = month.yearMonth
                daysOfWeek.forEach { daysOfWeek ->
                    val textView = DynamicTextView(requireContext())
                    textView.setTextAppearance(
                        requireContext(),
                        R.style.TextAppearance_MaterialComponents_Subtitle2
                    )
                    textView.text = daysOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    textView.layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                            weight = 1f
                        }
                    textView.setTextColor(colorText)
                    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    holder.binding.legend.addView(textView)
                }
            }
        }
    }


    private fun selectMonth(month: Month) {
        binding.monthSpinner.isChecked = false
        selectedViewDate = LocalDate.of(selectedViewDate.year, month.value, 1)
        binding.calendarView.scrollToDate(selectedViewDate)
        updateMonthSpinner()
        switchToCalendarView()
    }

    private fun selectYear(year: Year) {
        binding.yearSpinner.isChecked = false
        selectedViewDate = LocalDate.of(year.value, selectedViewDate.month.value, 1)
        binding.calendarView.scrollToDate(selectedViewDate)
        monthAdapter.updateCurrentYearMonth(selectedViewDate.yearMonth)
        updateYearSpinner()
        switchToCalendarView()
    }


    private fun switchToMonthsView() {
        calendarViewActive = false
        with(binding) {
            calendarView.visibility = View.INVISIBLE
            monthsRecyclerView.visibility = View.VISIBLE
            yearsRecyclerView.visibility = View.GONE
            if (calendarMode != CalendarMode.MONTH) {
                monthsRecyclerView.post {
                    monthLayoutManger.scrollToPositionWithOffset(
                        selectedViewDate.month.ordinal,
                        binding.monthsRecyclerView.height.div(2)
                    )
                }
            }
        }
    }


    private fun switchToYearsView() {
        calendarViewActive = false
        with(binding) {
            calendarView.visibility = View.INVISIBLE
            yearsRecyclerView.visibility = View.VISIBLE
            monthsRecyclerView.visibility = View.GONE
            val years = getYears()
            val index = years.indexOfFirst { it.value == selectedViewDate.year }
            yearsRecyclerView.post {
                yearLayoutManger.scrollToPositionWithOffset(
                    index,
                    binding.yearsRecyclerView.height.div(2)
                )
            }
        }
    }

    fun getSelectionShapeStartTransitionDrawable(bgAlpha: Float): LayerDrawable {
        selectionShapeStartBg.apply { alpha = bgAlpha.toInt() }
        return LayerDrawable(arrayOf(selectionShapeStart, selectionShapeStartBg))
    }

    private fun getYears(): MutableList<Year> {

        val years = mutableListOf<Year>()

        val rangePast = if (!disablePast) -rangeYears..-1L else null
        val rangeFuture = if (!disableFuture) 1L..rangeYears else null

        val now = Year.now()
        rangePast?.forEach { years.add(now.plusYears(it)) }
        years.add(now)
        rangeFuture?.forEach { years.add(now.plusYears(it)) }

        return years
    }

    private fun getDaysOfWeek(): Array<DayOfWeek> {
        val firstDay = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        if (firstDay != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDay.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDay.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    private fun switchToCalendarView() {
        calendarViewActive = true
        with(binding) {
            calendarView.visibility = View.VISIBLE
            yearsRecyclerView.visibility = View.GONE
            monthsRecyclerView.visibility = View.GONE
        }
    }

    private fun setCurrentDateText(date: LocalDate?) {
        with(binding) {
            dateSelected.text = date?.let { fullDate.format(date) } ?: "Select date"
        }
    }

    private fun setCurrentDateRangeText(dateStart: LocalDate?, dateEnd: LocalDate?) {
        with(binding) {
            val sameMonth = dateStart?.monthValue == dateEnd?.monthValue
            val rangeStartText = dateStart?.let {
                if (sameMonth) dateRangeStartNoMonth.format(it) else dateRangeStart.format(it)
            } ?: getString(R.string.date_range_from)
            val rangeEndText =
                dateEnd?.let { dateRangeStart.format(it) } ?: getString(R.string.date_range_to)
            dateSelected.text = getString(R.string.day_range_string, rangeStartText, rangeEndText)
        }
    }


    private fun updateSpinnerValues() {
        updateMonthSpinner()
        updateYearSpinner()
    }

    private fun updateYearSpinner() {
        binding.valueSpinnerYear.text = yearFormatter.format(selectedViewDate)
    }

    private fun updateMonthSpinner() {
        binding.valueSpinnerMonth.text = monthFormatter.format(selectedViewDate)
    }

    private fun validate() {

        val maxRangeLengthDays = TimeUnit.DAYS.toSeconds(maxRange.toLong())

        val selectionInRange = selectedDateEnd != null && selectedDateEnd!!.atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC).minus(
                selectedDateStart!!.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
            ) < maxRangeLengthDays

        val selectionValid = selectionMode == SelectionMode.RANGE && selectionInRange
                || selectionMode == SelectionMode.DATE && selectedDate != null

        if (!displayButtons && selectionValid) {
            Handler(Looper.getMainLooper()).postDelayed({
                save()
            }, 600)
        } else displayButtonPositive(selectionValid)
    }


    private fun save() {
        val selectedDateCalendar = selectedDate
        val selectedDateRangeStartCalendar = selectedDateStart
        val selectedDateRangeEndCalendar = selectedDateEnd

        listener?.invoke(
            selectedDateCalendar ?: selectedDateRangeStartCalendar!!,
            selectedDateRangeEndCalendar
        )

        drawableAnimator?.cancel()
        dismiss()
    }


    /** Build and show [CalendarViewBottomSheetDialog] directly. */
    fun show(
        ctx: Context,
        func: CalendarViewBottomSheetDialog.() -> Unit
    ): CalendarViewBottomSheetDialog {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }

    enum class SelectionMode {
        DATE,
        RANGE
    }

    enum class TimeLine {
        PAST,
        FUTURE
    }

    enum class CalendarMode(internal val rows: Int) {
        WEEK_1(1),
        WEEK_2(2),
        WEEK_3(3),
        MONTH(6)
    }


    internal class MonthViewHolder(val binding: CalendarHeaderItemBinding) : ViewContainer(binding.root)

    internal class DayViewHolder(view: View, listener: (CalendarDay) -> Unit) : ViewContainer(view) {
        val binding = CalendarDayItemBinding.bind(view)
        lateinit var day: CalendarDay

        init {
            view.setOnClickListener { listener.invoke(day) }
        }
    }
}