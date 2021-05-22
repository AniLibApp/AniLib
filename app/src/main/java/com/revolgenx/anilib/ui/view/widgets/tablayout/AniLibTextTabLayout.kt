package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.Defaults
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary

open class AniLibTextTabLayout : DynamicTabLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.AniLibTextTabLayout)
        try {
            val entries: Array<CharSequence> = a.getTextArray(R.styleable.AniLibTextTabLayout_entries)
            entries.forEach {
                addTab(this.newTab().setText(it))
            }
        } catch (_: Exception) {
        } finally {
            a.recycle()
        }
    }

}