package com.revolgenx.anilib.ui.view.widgets.misc

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R

open class BackgroundStrokeLayout : DynamicFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val dynamicTheme = DynamicTheme.getInstance().get()

        val strokeDrawable: GradientDrawable =
            ContextCompat.getDrawable(context, R.drawable.stroke_background) as GradientDrawable
        strokeDrawable.setStroke(
            2,
            DynamicColorUtils.getContrastColor(
                dynamicTheme.accentColor,
                dynamicTheme.backgroundColor
            )
        )
        background = strokeDrawable
    }
}