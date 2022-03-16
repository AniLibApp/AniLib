package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicAppBarLayout

class AlAppBarLayout : DynamicAppBarLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun applyWindowInsets() {
        val top = paddingTop
        val left = paddingLeft
        val right = paddingRight

        ViewCompat.setOnApplyWindowInsetsListener(
            this
        ) { v, insets ->
            v.setPadding(
                left + insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                top,
                right + insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                v.paddingBottom
            )
            insets
        }
    }
}