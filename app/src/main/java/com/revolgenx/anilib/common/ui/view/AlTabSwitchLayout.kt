package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.annotation.ArrayRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.pranavpandey.android.dynamic.support.Defaults
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.*
import com.revolgenx.anilib.util.sp

class AlTabSwitchLayout : TabLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        setColor()
        loadAttribute(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        setColor()
        loadAttribute(attributeSet)
    }

    private fun loadAttribute(attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.AlTabSwitchLayout)
        try {
            val entries: Array<CharSequence>? =
                a.getTextArray(R.styleable.AlTabSwitchLayout_entries)
            entries?.map { it.toString() }?.let {
                setEntries(it)
            }
        } catch (_: Exception) {
        } finally {
            a.recycle()
        }
    }

    fun setEntries(@ArrayRes entries: Int) {
        val entriesItem = context.theme.resources.getStringArray(entries).toList()
        setEntries(entriesItem)
    }

    fun setEntries(entries: List<String>) {
        entries.forEach {
            addTab(this.newTab().setText(it))
        }
    }

    private fun setColor() {
        val indicator =
            DynamicResourceUtils.getDrawable(context, R.drawable.al_tabs_indicator_corner)
        indicator?.bounds = tabSelectedIndicator.bounds
        setSelectedTabIndicator(indicator)
        setBackgroundColor(dynamicSurfaceColor)
        tabRippleColor = null
        setSelectedTabIndicatorColor(dynamicAccentColor)
        setTabTextColors(
            DynamicColorUtils.adjustAlpha(
                dynamicTextColorPrimary,
                Defaults.ADS_ALPHA_UNCHECKED
            ), dynamicTextColorOverAccent
        )
    }

}