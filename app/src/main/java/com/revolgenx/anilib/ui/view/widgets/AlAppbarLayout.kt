package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.pranavpandey.android.dynamic.support.widget.DynamicAppBarLayout
import kotlin.math.abs

class AlAppbarLayout : DynamicAppBarLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            val firstChild = getChildAt(0)
            val height = firstChild.height / 1.3f

            if (abs(verticalOffset) >= height) {
                visibility = View.GONE
            }else{
                visibility = View.VISIBLE
            }
        })
    }
}