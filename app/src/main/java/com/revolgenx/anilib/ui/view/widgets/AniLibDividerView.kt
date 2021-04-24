package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.app.theme.dynamicTintBackgroundColor

class AniLibDividerView:View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        setBackgroundColor(DynamicColorUtils.setAlpha(dynamicTintBackgroundColor, 120))
    }

}