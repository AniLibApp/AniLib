package com.revolgenx.anilib.home.discover.fragment

import android.view.View
import androidx.annotation.DrawableRes

interface BaseDiscoverHelper {
    fun addView(
        discoverChildView: View,
        title: String,
        showSetting: Boolean = false,
        @DrawableRes icon:Int? = null,
        onClick: ((which: Int) -> Unit)? = null
    )

    fun reloadAll()
}
