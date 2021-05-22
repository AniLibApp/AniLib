package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.view.setMargins
import com.pranavpandey.android.dynamic.support.Defaults
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import java.util.*

class DynamicDslWeekTabLayout : DynamicTabLayout {

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
            val t = getChildAt(0)
            val lp = (t.layoutParams as MarginLayoutParams)
            lp.setMargins(0)
            t.layoutParams = lp
        }

    }



    override fun setTextColor() {
        if (textColor != Theme.Color.UNKNOWN) {
            setTabTextColors(
                DynamicColorUtils.adjustAlpha(
                    dynamicTextColorPrimary,
                    Defaults.ADS_ALPHA_UNCHECKED
                ), contrastAccentWithBg
            )
            tabRippleColor = DynamicResourceUtils.getColorStateList(
                Color.TRANSPARENT, DynamicColorUtils.adjustAlpha(
                    contrastAccentWithBg, Defaults.ADS_ALPHA_PRESSED
                ), false
            )
            DynamicMenuUtils.setViewItemsTint(
                this,
                contrastAccentWithBg, dynamicBackgroundColor, false
            )
        }
    }

}