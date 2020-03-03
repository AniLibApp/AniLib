package com.revolgenx.anilib.fragment.base

import android.content.Context
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment

abstract class BasePagerFragment : BaseFragment() {
    open fun title(context: Context): String? = null
}