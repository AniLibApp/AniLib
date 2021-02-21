package com.revolgenx.anilib.ui.view.drawable

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor

class DynamicBackgroundGradientDrawable(orientation:Orientation = Orientation.LEFT_RIGHT, alpha:Int = 127):GradientDrawable(orientation, intArrayOf(DynamicColorUtils.setAlpha(dynamicBackgroundColor, alpha), Color.TRANSPARENT))