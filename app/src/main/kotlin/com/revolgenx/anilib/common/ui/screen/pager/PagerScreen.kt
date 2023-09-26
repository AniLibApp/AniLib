package com.revolgenx.anilib.common.ui.screen.pager

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

data class PagerScreen<T>(
    val type: T,
    @StringRes val title: Int,
    val icon: ImageVector? = null,
    val isVisible: MutableState<Boolean> = mutableStateOf(true)
)
