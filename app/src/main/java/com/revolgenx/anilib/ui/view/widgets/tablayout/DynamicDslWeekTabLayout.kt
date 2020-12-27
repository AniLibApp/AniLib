package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import java.util.*

class DynamicDslWeekTabLayout : DynamicTabLayout {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {

        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
        val week = context.resources.getStringArray(R.array.days_of_week)

        val weekFromToday = mutableListOf<String>()

        for(i in 0 until 7){
            var newDay = today + i
            (newDay % 7).also { newDay = it }
            weekFromToday.add(week[newDay])
        }

        weekFromToday.forEach {
            addTab(this.newTab().setText(it))
        }


        val selectedColor = DynamicColorUtils.getContrastColor(
            dynamicTheme.accentColor,
            dynamicTheme.backgroundColor
        )

        setTabTextColors(dynamicTheme.textPrimaryColor, selectedColor)
    }

}