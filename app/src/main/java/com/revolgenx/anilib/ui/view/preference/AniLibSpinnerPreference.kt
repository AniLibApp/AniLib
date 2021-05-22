package com.revolgenx.anilib.ui.view.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import java.lang.Exception

class AniLibSpinnerPreference: DynamicSpinnerPreference {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val dynamicTheme = DynamicTheme.getInstance().get()
        val primaryTextColor = dynamicTheme.textPrimaryColor
        titleView!!.setTextColor(primaryTextColor)

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.AniLibSpinnerPreference,
            defStyle,
            0
        )

        try {
            titleView!!.isAllCaps = a.getBoolean(R.styleable.AniLibSpinnerPreference_titleTextAllCaps, false)
        }catch (ex:Exception){}
        finally {
            a.recycle()
        }
        titleView!!.textSize = 14f
        findViewById<ImageView>(com.pranavpandey.android.dynamic.support.R.id.ads_preference_icon)?.setColorFilter(dynamicTheme.textPrimaryColor)
    }
}