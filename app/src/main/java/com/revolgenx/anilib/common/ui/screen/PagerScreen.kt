package com.revolgenx.anilib.common.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PagerScreen<T>(
    val type: T,
    @StringRes val title: Int,
    @DrawableRes val icon: Int? = null,
    var isVisible: Boolean = true
)
