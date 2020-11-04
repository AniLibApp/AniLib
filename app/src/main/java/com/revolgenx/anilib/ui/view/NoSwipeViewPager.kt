package com.revolgenx.anilib.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.pranavpandey.android.dynamic.support.widget.DynamicViewPager

class NoSwipeViewPager(context: Context, attributeSet: AttributeSet?) :
    DynamicViewPager(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}