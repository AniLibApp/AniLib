package com.revolgenx.anilib.fragment.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment

abstract class BaseFragment : DynamicFragment() {

    companion object {
        fun <T : BaseFragment> newInstance(clz: Class<T>) = clz.newInstance()
        fun <T : BaseFragment> newInstances(clzzez: List<Class<out T>>) =
            clzzez.map { it.newInstance() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun invalidateOptionMenu() {
        (activity as AppCompatActivity).invalidateOptionsMenu()
    }
}