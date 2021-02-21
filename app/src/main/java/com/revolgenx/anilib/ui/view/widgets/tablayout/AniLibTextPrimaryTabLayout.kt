package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils

class AniLibTextPrimaryTabLayout : AniLibTextTabLayout {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val selectedColor = DynamicColorUtils.getContrastColor(
            dynamicTheme.accentColor,
            dynamicTheme.backgroundColor
        )
        setTabTextColors(dynamicTheme.textPrimaryColor, selectedColor)
    }
}