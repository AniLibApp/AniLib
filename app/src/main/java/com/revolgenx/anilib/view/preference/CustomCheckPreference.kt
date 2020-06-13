package com.revolgenx.anilib.view.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pranavpandey.android.dynamic.support.R
import com.pranavpandey.android.dynamic.support.setting.DynamicCheckPreference
import com.revolgenx.anilib.util.dp

class CustomCheckPreference : DynamicCheckPreference {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        findViewById<LinearLayout?>(R.id.ads_preference)?.setPadding(dp(14f),dp(4f),dp(14f),dp(4f))
        findViewById<TextView?>(R.id.ads_preference_title)?.textSize = 12f
        findViewById<ImageView?>(R.id.ads_preference_icon)?.visibility = View.GONE
    }
}