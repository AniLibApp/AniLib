package com.revolgenx.anilib.common.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PagerScreen<T>(
    val type: T,
    @StringRes val title: Int,
    @DrawableRes val icon: Int? = null,
    val isVisible: MutableState<Boolean> = mutableStateOf(true)
)
