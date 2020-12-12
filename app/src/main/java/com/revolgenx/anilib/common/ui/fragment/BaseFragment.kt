package com.revolgenx.anilib.common.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment

abstract class BaseFragment : DynamicFragment() {
    fun invalidateOptionMenu() {
        (activity as AppCompatActivity).invalidateOptionsMenu()
    }
}