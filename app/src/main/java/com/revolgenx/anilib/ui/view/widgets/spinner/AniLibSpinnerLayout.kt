package com.revolgenx.anilib.ui.view.widgets.spinner

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicSpinner
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import java.lang.Exception

class AniLibSpinnerLayout : DynamicFrameLayout {
    val spinnerView: DynamicSpinner

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val dynamicTheme = DynamicTheme.getInstance().get()

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.AniLibSpinner,
            defStyle,
            0
        )
        try {
            val addStroke = a.getBoolean(R.styleable.AniLibSpinner_add_stroke, true)
            if (addStroke) {
                val strokeDrawable: GradientDrawable =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.stroke_background
                    ) as GradientDrawable
                strokeDrawable.setStroke(
                    2,
                    DynamicColorUtils.getContrastColor(
                        dynamicTheme.accentColor,
                        dynamicTheme.backgroundColor
                    )
                )
                background = strokeDrawable
            }
        } catch (_: Exception) {

        } finally {
            a.recycle()
        }

        spinnerView = DynamicSpinner(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addView(spinnerView)
    }

}