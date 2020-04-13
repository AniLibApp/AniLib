package com.revolgenx.anilib.view.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

abstract class BaseDrawablePaint : BaseDrawable() {
    private var mPaint: Paint? = null

    override fun onDraw(canvas: Canvas?, width: Int, height: Int) {
        if (mPaint == null) {
            mPaint = Paint()
            mPaint!!.isAntiAlias = true
            mPaint!!.color = Color.WHITE
            onPreparePaint(mPaint)
        }
        mPaint!!.alpha = mAlpha
//        mPaint!!.colorFilter = getColorFilterForDrawing()
        onDraw(canvas, width, height, mPaint)
    }

    protected abstract fun onPreparePaint(paint: Paint?)

    protected abstract fun onDraw(
        canvas: Canvas?,
        width: Int,
        height: Int,
        paint: Paint?
    )
}