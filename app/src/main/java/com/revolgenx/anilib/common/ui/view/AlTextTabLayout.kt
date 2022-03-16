package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicTabLayout
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget
import com.revolgenx.anilib.R

open class AlTextTabLayout : DynamicTabLayout, WindowInsetsWidget {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.AlTextTabLayout)
        try {
            val entries: Array<CharSequence>? = a.getTextArray(R.styleable.AlTextTabLayout_entries)
            entries?.forEach {
                addTab(this.newTab().setText(it))
            }

            val applyInset = a.getBoolean(R.styleable.AlTextTabLayout_apply_inset, false)
            if (applyInset) {
                applyWindowInsets()
            }
        } catch (_: Exception) {
        } finally {
            a.recycle()
        }
    }

    final override fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            (v.layoutParams as MarginLayoutParams).also {
                it.setMargins(
                    0,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    0,
                    0
                )
            }
            insets
        }
    }

}