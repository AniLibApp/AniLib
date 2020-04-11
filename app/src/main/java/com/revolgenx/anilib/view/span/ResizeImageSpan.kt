package com.revolgenx.anilib.view.span

import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference


class ResizeImageSpan(d: Drawable, source: String) : ImageSpan(d, source) {

    private val mContainerWidth = 0

    companion object {
        private const val MIN_SCALE_WIDTH = 240
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        val d = getCachedDrawable()
        val rect: Rect = getResizedDrawableBounds(d)
        if (fm != null) {
            fm.ascent = -rect.bottom
            fm.descent = 0
            fm.top = fm.ascent
            fm.bottom = 0
        }
        return rect.right
    }

    private fun getResizedDrawableBounds(d: Drawable?): Rect {
        if (d == null || d.intrinsicWidth == 0) {
            return Rect(0, 0, d!!.intrinsicWidth, d.intrinsicHeight)
        }
        val scaledHeight: Int
        if (d.intrinsicWidth < mContainerWidth) { // Image smaller than container's width.
            if (d.intrinsicWidth > MIN_SCALE_WIDTH &&
                d.intrinsicWidth >= d.intrinsicHeight
            ) { // But larger than the minimum scale size, we need to scale the image to fit
// the width of the container.
                val scaledWidth: Int = mContainerWidth
                scaledHeight = d.intrinsicHeight * scaledWidth / d.intrinsicWidth
                d.setBounds(0, 0, scaledWidth, scaledHeight)
            } else { // Smaller than the minimum scale size, leave it as is.
                d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
            }
        } else { // Image is larger than the container's width, scale down to fit the container.
            val scaledWidth: Int = mContainerWidth
            scaledHeight = d.intrinsicHeight * scaledWidth / d.intrinsicWidth
            d.setBounds(0, 0, scaledWidth, scaledHeight)
        }
        return d.bounds
    }

    private fun getCachedDrawable(): Drawable? {
        val wr: WeakReference<Drawable>? = mDrawableRef
        var d: Drawable? = null
        if (wr != null) {
            d = wr.get()
        }
        if (d == null) {
            d = drawable
            mDrawableRef = WeakReference<Drawable>(d)
        }
        return d
    }

    private var mDrawableRef: WeakReference<Drawable>? = null
}