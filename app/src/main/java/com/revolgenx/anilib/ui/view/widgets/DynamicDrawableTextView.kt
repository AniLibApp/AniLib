package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils

class DynamicDrawableTextView : DynamicTextView {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
    }

    fun setDrawables(@DrawableRes startRes: Int?, @DrawableRes endRes: Int?, color: Int? = null) {
        val startDrawable = startRes?.let {
            ContextCompat.getDrawable(context, startRes)
                .also {
                    DynamicDrawableUtils.colorizeDrawable(
                        it,
                        color ?: dynamicTheme.textPrimaryColor
                    )
                }
        }

        val endDrawable = endRes?.let {
            ContextCompat.getDrawable(context, endRes)
                .also {
                    DynamicDrawableUtils.colorizeDrawable(
                        it,
                        color ?: dynamicTheme.textPrimaryColor
                    )
                }
        }

        this.setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, null, endDrawable, null)
    }

}