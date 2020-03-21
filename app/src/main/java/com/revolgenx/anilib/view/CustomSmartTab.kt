package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
import com.ogaclejapan.smarttablayout.SmartTabLayout

class CustomSmartTab(context: Context, attributeSet: AttributeSet?, style: Int) :
    SmartTabLayout(context, attributeSet, style) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {

    }

    fun getTabs(): Sequence<View> {
        return tabStrip.children
    }

    fun setViewPager(viewPager: ViewPager?, callback: ((position: Int) -> Unit)? = null) {
        super.setViewPager(viewPager)
        getTabs().forEach { tabs ->
            tabs.setOnClickListener {
                for (i in 0 until tabStrip.children.count()) {
                    if (it === getTabAt(i)) {
                        callback?.invoke(i)
                        viewPager!!.setCurrentItem(i, false)
                        return@setOnClickListener
                    }
                }
            }
        }
    }
}