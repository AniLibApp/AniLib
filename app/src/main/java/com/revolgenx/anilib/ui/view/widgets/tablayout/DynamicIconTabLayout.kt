package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.pranavpandey.android.dynamic.support.widget.WidgetDefaults
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.app.theme.contrastAccentWithPrimary
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.app.theme.dynamicTintPrimaryColor

class DynamicIconTabLayout: DynamicTabLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet){
    }


    override fun setTextColor() {
        tabRippleColor = DynamicResourceUtils.getColorStateList(
            Color.TRANSPARENT, DynamicColorUtils.adjustAlpha(
                contrastAccentWithPrimary, WidgetDefaults.ADS_ALPHA_PRESSED
            ), false
        )
        setTabTextColors(dynamicTintPrimaryColor, contrastAccentWithPrimary);
        tabIconTint = createColorStateList(dynamicTintPrimaryColor, contrastAccentWithPrimary)
    }

    private fun createColorStateList(defaultColor: Int, selectedColor: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        val colors = IntArray(2)
        var i = 0
        states[i] = SELECTED_STATE_SET
        colors[i] = selectedColor
        i++

        // Default enabled state
        states[i] = EMPTY_STATE_SET
        colors[i] = defaultColor
        return ColorStateList(states, colors)
    }
}