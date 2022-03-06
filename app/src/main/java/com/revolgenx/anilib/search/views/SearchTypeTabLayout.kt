package com.revolgenx.anilib.search.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import com.pranavpandey.android.dynamic.support.Defaults
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.*

class SearchTypeTabLayout : TabLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        setColor()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        setColor()
    }

    private fun setColor() {
        val indicator =
            DynamicResourceUtils.getDrawable(context, R.drawable.al_tabs_indicator_corner)?.also {
                (it as GradientDrawable).cornerRadius = dynamicCornerRadius.toFloat()
            }
        indicator?.bounds = tabSelectedIndicator.bounds
        setSelectedTabIndicator(indicator)

        setBackgroundColor(dynamicBackgroundColor)
        tabRippleColor = null
        setSelectedTabIndicatorColor(dynamicAccentColor)
        setTabTextColors(
            DynamicColorUtils.adjustAlpha(
                dynamicTextColorPrimary,
                Defaults.ADS_ALPHA_UNCHECKED
            ), dynamicTextColorOverAccent
        )
    }

}