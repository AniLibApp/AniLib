package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.revolgenx.anilib.R

class DynamicTextTabLayout :DynamicTabLayout{

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicTextTabLayout)
        try {
            val entries: Array<CharSequence> = a.getTextArray(R.styleable.DynamicTextTabLayout_entries)
            entries.forEach {
                addTab(this.newTab().setText(it))
            }
        } catch(_:Exception){}
        finally {
            a.recycle()
        }


        setTabTextColors(dynamicTheme.textPrimaryColor, dynamicTheme.accentColor)
    }
}