package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget

class AlFrameLayout : DynamicFrameLayout, WindowInsetsWidget {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun loadFromAttributes(attrs: AttributeSet?) {
        super.loadFromAttributes(attrs)
        applyWindowInsets()
    }

    override fun applyWindowInsets() {
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