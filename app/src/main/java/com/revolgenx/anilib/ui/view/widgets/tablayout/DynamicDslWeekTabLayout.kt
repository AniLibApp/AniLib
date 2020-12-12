package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import com.angcyo.tablayout.DslTabLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.view.makeToast
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
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

        setTabTextColors(dynamicTheme.textPrimaryColor, dynamicTheme.accentColor)
    }

}