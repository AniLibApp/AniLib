package com.revolgenx.anilib.ui.view.drawable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.text.TextPaint
import androidx.core.content.res.ResourcesCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.sp

class GifDrawable(private val context: Context, bitmap: Bitmap) :
    BitmapDrawable(context.resources, bitmap) {
    private val paint = TextPaint()
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        paint.reset()
        paint.color = Color.WHITE
        val textSize = sp(18f * if (bounds.width() > 300) 1 else bounds.width() / 300)
        paint.textSize = textSize
        paint.typeface = ResourcesCompat.getFont(context, R.font.berlinrounded_extra_bold)
        val txtWidth = paint.measureText(context.getString(R.string.gif)) + 20
        val txtHeight = paint.fontMetrics.bottom - paint.fontMetrics.top

        val left = bounds.width() / 2 - txtWidth / 2
        val top = bounds.height() / 2


        canvas.drawCircle(bounds.width() / 2f, bounds.height() / 2f, txtWidth, paint.also {
            it.style = Paint.Style.STROKE
            it.strokeWidth = SpoilerDrawable.strokeWidth.toFloat()
        })

        canvas.drawText(
            context.getString(R.string.gif),
            left, top + txtHeight / 3, paint.also {
                it.style = Paint.Style.FILL
            }
        )
    }
}