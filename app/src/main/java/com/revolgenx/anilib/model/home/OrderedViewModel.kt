package com.revolgenx.anilib.model.home

import android.view.View

data class OrderedViewModel(
    val oView: View,
    val order: Int,
    val title: String,
    val showSetting: Boolean,
    val onClick: ((which: Int) -> Unit)? = null
)