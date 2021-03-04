package com.revolgenx.anilib.ui.view.search

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import com.paulrybitskyi.persistentsearchview.PersistentSearchView

class DynamicPersistentSearchView : PersistentSearchView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet, 0) {
    }
    constructor(context: Context, attributeSet: AttributeSet?, defStyle:Int):super(context, attributeSet, defStyle)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val measuredSize: IntArray = calculateTheUsedSpace(widthMeasureSpec)
        setMeasuredDimension(measuredSize[0], measuredSize[1])
    }

    private fun calculateTheUsedSpace(widthMeasureSpec: Int): IntArray {
        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = findViewById<CardView>(com.paulrybitskyi.persistentsearchview.R.id.cardView).measuredHeight + paddingTop + paddingBottom
        return intArrayOf(measuredWidth, measuredHeight)
    }

}