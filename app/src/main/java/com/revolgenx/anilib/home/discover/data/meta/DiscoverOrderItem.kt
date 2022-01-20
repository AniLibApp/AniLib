package com.revolgenx.anilib.home.discover.data.meta

import android.view.View

data class DiscoverOrderItem(
    val oView: View,
    val order: Int,
    val title: String,
    val icon:Int? = null,
    val showSetting: Boolean,
    val onClick: ((which: Int) -> Unit)? = null
)