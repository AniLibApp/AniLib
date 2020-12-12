package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.angcyo.tablayout.DslTabIndicator
import com.angcyo.tablayout.DslTabLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R

class DynamicChipDslTabLayout : DslTabLayout {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {


        val a = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicChipDslTabLayout)
        val entries: Array<CharSequence>
        try {
            entries = a.getTextArray(R.styleable.DynamicChipDslTabLayout_entries)
        } finally {
            a.recycle()
        }

        entries.iterator().forEach {
            val dynamicTextView =
                DynamicTextView(ContextThemeWrapper(context, R.style.DslTabLayoutChipStyle))
            dynamicTextView.text = it
            addView(dynamicTextView)
        }

        setBackgroundColor(dynamicTheme.backgroundColor)

        tabIndicator.indicatorDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_chip_background).let {
                DynamicDrawableUtils.colorizeDrawable(it, dynamicTheme.accentColor)
            }

        tabIndicator.indicatorStyle = DslTabIndicator.INDICATOR_STYLE_BACKGROUND
        tabIndicator.indicatorHeight = -2
        tabIndicator.indicatorWidth = -2
        tabIndicator.indicatorWidthOffset = DynamicUnitUtils.convertDpToPixels(15f)
        tabIndicator.indicatorHeightOffset = DynamicUnitUtils.convertDpToPixels(-10f)

        configTabLayoutConfig {
            this.tabSelectColor = DynamicColorUtils.getContrastColor(
                dynamicTheme.textPrimaryColor,
                dynamicTheme.accentColor
            )
            this.tabDeselectColor = dynamicTheme.textPrimaryColor
        }

    }


}