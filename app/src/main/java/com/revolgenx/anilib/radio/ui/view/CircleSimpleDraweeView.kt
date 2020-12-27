package com.revolgenx.anilib.radio.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R

class CircleSimpleDraweeView : SimpleDraweeView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CircleSimpleDraweeView,
            defStyle,
            0
        )

        hierarchy.setBackgroundImage(ColorDrawable(Color.WHITE))
        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_INSIDE
        try {
            setBorder =
                a.getBoolean(R.styleable.CircleSimpleDraweeView_showBorder, false)
        } finally {
            a.recycle()
        }
    }

    var setBorder: Boolean = false
        set(value) {
            field = value
            hierarchy.roundingParams = if (value) roundingParamsWithBorder else roundingParams
            invalidate()
        }

    private val roundingParams by lazy {
        RoundingParams.asCircle()
    }

    private val roundingParamsWithBorder
        get() = roundingParams.also {
            it.borderWidth = 6f
            it.borderColor = DynamicTheme.getInstance().get().accentColor
        }


}