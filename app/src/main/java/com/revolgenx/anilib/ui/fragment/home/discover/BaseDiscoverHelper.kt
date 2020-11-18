package com.revolgenx.anilib.ui.fragment.home.discover

import android.view.View

interface BaseDiscoverHelper {
    fun addView(
        view: View,
        title: String,
        showSetting: Boolean = false,
        onClick: ((which: Int) -> Unit)? = null
    )

    fun reloadAll()
}
