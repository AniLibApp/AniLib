package com.revolgenx.anilib.ui.view.preference

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pranavpandey.android.dynamic.support.setting.DynamicCheckPreference
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.sp

class CustomCheckPreference : DynamicCheckPreference {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomCheckPreference,
            0, 0
        )

        val ctv = findViewById<TextView?>(com.pranavpandey.android.dynamic.support.R.id.ads_preference_title)

        try {
            val tvDimen = a.getDimensionPixelSize(R.styleable.CustomCheckPreference_text_size, sp(12f).toInt())
            ctv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvDimen.toFloat())
        } finally {
            a.recycle()
        }

        findViewById<LinearLayout?>(com.pranavpandey.android.dynamic.support.R.id.ads_preference)?.setPadding(dp(14f),dp(4f),dp(14f),dp(4f))
        findViewById<ImageView?>(com.pranavpandey.android.dynamic.support.R.id.ads_preference_icon)?.visibility = View.GONE
    }
}