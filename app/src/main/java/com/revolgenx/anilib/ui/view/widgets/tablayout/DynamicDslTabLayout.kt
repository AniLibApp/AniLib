package com.revolgenx.anilib.ui.view.widgets.tablayout


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import com.angcyo.tablayout.DslTabLayout
import com.pranavpandey.android.dynamic.locale.DynamicLocaleUtils
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R

class DynamicDslTabLayout : DslTabLayout {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicDslTabLayout)
        val entries: Array<CharSequence>
        try {
            entries = a.getTextArray(R.styleable.DynamicDslTabLayout_entries)
        } finally {
            a.recycle()
        }

        entries.iterator().forEach {
            val dynamicTextView =
                DynamicTextView(ContextThemeWrapper(context, R.style.DslTabLayoutTextStyle))
            dynamicTextView.text = it
            addView(dynamicTextView)
        }


        setBackgroundColor(dynamicTheme.backgroundColor)
        configTabLayoutConfig {
            this.tabSelectColor = dynamicTheme.accentColor
            this.tabDeselectColor = dynamicTheme.textPrimaryColor
            this.tabEnableGradientScale = true
            this.tabMaxScale = 1.1f
            this.tabMinScale = 1.0f
        }
    }


}