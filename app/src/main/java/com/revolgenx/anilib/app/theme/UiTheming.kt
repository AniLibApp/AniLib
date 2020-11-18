package com.revolgenx.anilib.app.theme

import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.util.dp

fun ToggleSwitch.themeIt(){
    uncheckedBackgroundColor = ThemeController.lightSurfaceColor()
    checkedBackgroundColor = DynamicTheme.getInstance().get().accentColor
    borderRadius = dp(10f).toFloat()
    toggleElevation = dp(1f).toFloat()
    checkedTextColor = DynamicTheme.getInstance().get().tintAccentColor
    uncheckedTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
    reDraw()
}