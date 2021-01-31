package com.revolgenx.anilib.ui.view.widgets.chip

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils

class DynamicChip : Chip {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        val dynamicTheme = DynamicTheme.getInstance().get()
        val chipBgColor = ColorStateList.valueOf(dynamicTheme.backgroundColor)
        val tintAccent = DynamicColorUtils.getContrastColor(
            dynamicTheme.tintAccentColor,
            dynamicTheme.backgroundColor
        )
        val chipStColor = ColorStateList.valueOf(
            tintAccent
        )

        chipBackgroundColor = chipBgColor
        chipStrokeWidth = 2f
        chipStrokeColor = chipStColor
        isCloseIconVisible = true
        closeIconTint = chipStColor
        setTextColor(tintAccent)
    }

}