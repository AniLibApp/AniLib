package com.revolgenx.anilib.ui.view.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.pranavpandey.android.dynamic.support.setting.DynamicSpinnerPreference
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme

class AniLibSpinnerPreference:DynamicSpinnerPreference{
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val dynamicTheme = DynamicTheme.getInstance().get()
        val primaryTextColor = dynamicTheme.textPrimaryColor
        titleView.setTextColor(primaryTextColor)
        titleView.isAllCaps = true
        titleView.textSize = 12f
        findViewById<ImageView>(com.pranavpandey.android.dynamic.support.R.id.ads_preference_icon)?.setColorFilter(dynamicTheme.textPrimaryColor)
    }
}