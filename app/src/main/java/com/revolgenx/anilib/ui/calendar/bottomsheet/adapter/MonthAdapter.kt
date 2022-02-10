package com.revolgenx.anilib.ui.calendar.bottomsheet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.databinding.CalendarMonthItemBinding
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter


/** Listener that is invoked if a month is selected. */

internal class MonthAdapter(
    private val ctx: Context,
    private val disablePast: Boolean = false,
    private val disableFuture: Boolean = false,
    private var itemHeight: Int? = null,
    private val listener: (Month) -> Unit
) : RecyclerView.Adapter<MonthAdapter.MonthItem>() {

    private val calendarMonths = Month.values().mapTo(mutableListOf(), { it })

    private val formatter = DateTimeFormatter.ofPattern("MMMM")
    private var selectedMonth = YearMonth.now().month
    private var currentYearMonth = YearMonth.now()
    private var currentSelectedYear = YearMonth.now()

    private val contrastAccentWithBgColor = contrastAccentWithBg
    private val textColor = dynamicTextColorPrimary

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthItem =
        MonthItem(
            CalendarMonthItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MonthItem, i: Int) {
        val month = calendarMonths[i]
        itemHeight?.let { holder.binding.root.layoutParams.apply { height = it } }
        holder.binding.month.text = formatter.format(month)
        holder.binding.handleState(month)
        holder.binding.root.setOnClickListener {
            updateSelectedMonth(month)
            listener.invoke(month)
        }
    }

    private fun CalendarMonthItemBinding.handleState(monthAtIndex: Month) = when {
        disablePast && currentSelectedYear.year == currentYearMonth.year && monthAtIndex.value < currentYearMonth.month.value -> {
            month.isSelected = false
            month.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            month.setTextColor(textColor)
            month.alpha = 0.2f
        }
        disableFuture && currentSelectedYear.year == currentYearMonth.year && monthAtIndex.value > currentYearMonth.month.value -> {
            month.isSelected = false
            month.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            month.setTextColor(textColor)
            month.alpha = 0.2f
        }
        currentYearMonth.month == monthAtIndex && selectedMonth == monthAtIndex -> {
            month.isSelected = true
            month.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            month.setTextColor(contrastAccentWithBgColor)
            month.alpha = 1f
        }
        currentYearMonth.month == monthAtIndex -> {
            month.isSelected = true
            month.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Body2)
            month.setTextColor(contrastAccentWithBgColor)
            month.alpha = 1f
        }
        selectedMonth == monthAtIndex -> {
            month.isSelected = false
            month.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            month.setTextColor(contrastAccentWithBgColor)
            month.alpha = 1f
        }
        else -> {
            month.isSelected = false
            month.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Body2)
            month.setTextColor(textColor)
            month.alpha = 1f
        }
    }

    fun updateItemHeight(itemHeight: Int?) {
        this.itemHeight = itemHeight
        notifyDataSetChanged()
    }

    fun updateCurrentYearMonth(yearMonth: YearMonth) {
        currentSelectedYear = yearMonth
        notifyDataSetChanged()
    }

    private fun updateSelectedMonth(month: Month) {
        selectedMonth = month
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = calendarMonths.size

    inner class MonthItem(val binding: CalendarMonthItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

