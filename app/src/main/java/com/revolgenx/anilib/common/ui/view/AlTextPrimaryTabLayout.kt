package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.Defaults
import com.pranavpandey.android.dynamic.support.utils.DynamicMenuUtils
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary

class AlTextPrimaryTabLayout : AlTextTabLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
    }

    override fun setTextColor() {
        if (textColor != Theme.Color.UNKNOWN) {
            setTabTextColors(
                DynamicColorUtils.adjustAlpha(
                    dynamicTextColorPrimary,
                    Defaults.ADS_ALPHA_UNCHECKED
                ), dynamicTextColorPrimary
            )
            tabRippleColor = DynamicResourceUtils.getColorStateList(
                Color.TRANSPARENT, DynamicColorUtils.adjustAlpha(
                    contrastAccentWithBg, Defaults.ADS_ALPHA_PRESSED
                ), false
            )
            DynamicMenuUtils.setViewItemsTint(
                this,
                contrastAccentWithBg, dynamicBackgroundColor, false
            )
        }
    }
}