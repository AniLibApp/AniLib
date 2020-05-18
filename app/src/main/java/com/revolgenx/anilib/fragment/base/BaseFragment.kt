package com.revolgenx.anilib.fragment.base

import androidx.appcompat.app.AppCompatActivity
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment

abstract class BaseFragment : DynamicFragment() {

    companion object {
        fun <T : BaseFragment> newInstance(clz: Class<T>) = clz.newInstance()
        fun <T : BaseFragment> newInstances(clzzez: List<Class<out T>>) =
            clzzez.map { it.newInstance() }
    }


    fun invalidateOptionMenu() {
        (activity as AppCompatActivity).invalidateOptionsMenu()
    }
}