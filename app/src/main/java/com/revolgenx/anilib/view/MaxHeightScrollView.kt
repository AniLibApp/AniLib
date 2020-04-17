package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.widget.DynamicScrollView
import com.revolgenx.anilib.R
import timber.log.Timber


class MaxHeightScrollView : DynamicScrollView {
    private var maxHeight = WITHOUT_MAX_HEIGHT_VALUE

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0) {}
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MaxHeightScrollView,
            0, 0
        )

        try {
            setMaxHeight(a.getLayoutDimension(R.styleable.MaxHeightScrollView_maxHeight, -1))
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var hMeasureSpec = heightMeasureSpec
        try {
            var heightSize = MeasureSpec.getSize(hMeasureSpec)
            if (maxHeight != WITHOUT_MAX_HEIGHT_VALUE
                && heightSize > maxHeight
            ) {
                heightSize = maxHeight
            }
            hMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
            layoutParams.height = heightSize
        } catch (e: Exception) {
            Timber.w(e, "onMeasure:Error forcing height")
        } finally {
            super.onMeasure(widthMeasureSpec, hMeasureSpec)
        }
    }

    private fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
    }

    companion object {
        var WITHOUT_MAX_HEIGHT_VALUE = -1
    }
}