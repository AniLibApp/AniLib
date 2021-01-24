package com.revolgenx.anilib.ui.adapter.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.databinding.CalendarYearItemBinding
import java.time.Year
import java.time.format.DateTimeFormatter


/** Listener that is invoked if a year is selected. */
internal typealias YearSelectedListener = (Year) -> Unit

internal class YearAdapter(
    private val ctx: Context,
    private val years: MutableList<Year>,
    private val listener: YearSelectedListener
) : RecyclerView.Adapter<YearAdapter.YearItem>() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy")
    private var selectedYear = Year.now()
    private var currentYear = Year.now()


    private val contrastAccentWithBgColor = contrastAccentWithBg
    private val textColor = dynamicTextColorPrimary


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearItem =
        YearItem(
            CalendarYearItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: YearItem, i: Int) {
        val year = years[i]
        holder.binding.year.text = formatter.format(year.atDay(1))
        holder.binding.handleState(year)
        holder.binding.root.setOnClickListener {
            listener.invoke(year)
            updateSelectedYear(year)
        }
    }

    private fun CalendarYearItemBinding.handleState(yearAtIndex: Year) = when {
        currentYear == yearAtIndex && selectedYear == yearAtIndex -> {
            year.isSelected = true
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            year.setTextColor(contrastAccentWithBgColor)
        }
        currentYear == yearAtIndex -> {
            year.isSelected = true
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Body2)
            year.setTextColor(contrastAccentWithBgColor)
        }
        selectedYear == yearAtIndex -> {
            year.isSelected = false
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Subtitle2)
            year.setTextColor(contrastAccentWithBgColor)
        }
        else -> {
            year.isSelected = false
            year.setTextAppearance(ctx, R.style.TextAppearance_MaterialComponents_Body2)
            year.setTextColor(textColor)
        }
    }

    private fun updateSelectedYear(year: Year) {
        selectedYear = year
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = years.size

    inner class YearItem(val binding: CalendarYearItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}