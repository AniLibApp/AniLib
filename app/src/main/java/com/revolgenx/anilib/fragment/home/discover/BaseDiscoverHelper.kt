package com.revolgenx.anilib.fragment.home.discover

import android.view.View

interface BaseDiscoverHelper {
    fun addView(view: View, title:String,onClick: ((which: Int) -> Unit))
    fun reloadAll()
}
