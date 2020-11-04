package com.revolgenx.anilib.ui.view

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.revolgenx.anilib.util.dp


class MaxHeightFrameLayout(context: Context, attributeSet: AttributeSet?, def:Int) :DynamicFrameLayout(context, attributeSet, def){
    val maxHeight = dp(500f)
    constructor(context:Context):this(context, null)
    constructor(context:Context, attributeSet: AttributeSet?):this(context,attributeSet, 0){
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec1:Int = heightMeasureSpec
        if (maxHeight > 0) {
            val hSize = MeasureSpec.getSize(heightMeasureSpec1)
            val hMode = MeasureSpec.getMode(heightMeasureSpec1)
            when (hMode) {
                MeasureSpec.AT_MOST -> heightMeasureSpec1 = MeasureSpec.makeMeasureSpec(
                    Math.min(hSize, maxHeight),
                    MeasureSpec.AT_MOST
                )
                MeasureSpec.UNSPECIFIED -> heightMeasureSpec1 =
                    MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
                MeasureSpec.EXACTLY -> heightMeasureSpec1 = MeasureSpec.makeMeasureSpec(
                    Math.min(hSize, maxHeight),
                    MeasureSpec.EXACTLY
                )
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec1)
    }
}