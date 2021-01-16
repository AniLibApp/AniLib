package com.revolgenx.anilib.app.theme

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils

val dynamicTheme get() = DynamicTheme.getInstance().get()

val contrastAccentWithBg by lazy {
    DynamicColorUtils.getContrastColor(dynamicTheme.accentColor, dynamicTheme.backgroundColor)
}

val textColorPrimary get() = dynamicTheme.textPrimaryColor