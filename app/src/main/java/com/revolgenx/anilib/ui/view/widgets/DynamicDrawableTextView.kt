package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary

class DynamicDrawableTextView : DynamicTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
    }

    fun setDrawables(@DrawableRes startRes: Int?, @DrawableRes endRes: Int?,@DrawableRes topRes:Int? = null, @DrawableRes bottomRes:Int? = null, color: Int? = null) {

        val textPrimary = dynamicTextColorPrimary

        val startDrawable = startRes?.let {
            ContextCompat.getDrawable(context, startRes)
                .also {
                    DynamicDrawableUtils.colorizeDrawable(
                        it,
                        color ?: textPrimary
                    )
                }
        }

        val endDrawable = endRes?.let {
            ContextCompat.getDrawable(context, endRes)
                .also {
                    DynamicDrawableUtils.colorizeDrawable(
                        it,
                        color ?: textPrimary
                    )
                }
        }

        val topDrawable = topRes?.let {
            ContextCompat.getDrawable(context, topRes)
                .also {
                    DynamicDrawableUtils.colorizeDrawable(
                        it,
                        color ?: textPrimary
                    )
                }
        }

        val bottomDrawable = bottomRes?.let {
            ContextCompat.getDrawable(context, bottomRes)
                .also {
                    DynamicDrawableUtils.colorizeDrawable(
                        it,
                        color ?: textPrimary
                    )
                }
        }


        this.setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, topDrawable, endDrawable, bottomDrawable)
    }

}