package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.core.view.*
import com.pranavpandey.android.dynamic.support.widget.DynamicToolbar
import com.pranavpandey.android.dynamic.support.widget.base.WindowInsetsWidget

class AlToolbar : DynamicToolbar, WindowInsetsWidget {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?, @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


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