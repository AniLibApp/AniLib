package com.revolgenx.anilib.fragment.base

import android.content.Context
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment

abstract class BasePagerFragment : DynamicFragment() {

    companion object {
        fun <T : BasePagerFragment> newInstances(clzzez: List<Class<out T>>) =
            clzzez.map { it.newInstance() }
    }

    open fun title(context: Context): String? = null
}