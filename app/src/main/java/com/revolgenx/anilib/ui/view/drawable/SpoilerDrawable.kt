package com.revolgenx.anilib.ui.view.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.res.ResourcesCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.sp

class SpoilerDrawable(private val context: Context, bitmap: Bitmap) :
    BitmapDrawable(context.resources, bitmap) {
    var hasSpoiler = false
    private val rectF = RectF()


    companion object{
        val bottomPadding = dp(24f)
        val strokeWidth = dp(1.2f)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        paint.reset()
        paint.color = Color.WHITE
        paint.textSize = sp(18f * if(bounds.width() > 300) 1 else bounds.width() / 300)
        paint.typeface = ResourcesCompat.getFont(context, R.font.berlinrounded_extra_bold)
        val txtWidth = paint.measureText(context.getString(R.string.spoiler_alert))
        val txtHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
        rectF.left = bounds.width() / 2 - txtWidth / 2 - 30
        rectF.right = rectF.left + txtWidth + 60

        rectF.bottom = bounds.height() /1.15f
        rectF.top = rectF.bottom  - txtHeight/2 - bottomPadding

        canvas.drawRect(rectF, paint.also {
            it.style = Paint.Style.STROKE
            it.strokeWidth = strokeWidth.toFloat()
        })


        canvas.drawText(
            context.getString(R.string.spoiler_alert),
            rectF.left + 30, rectF.centerY() + txtHeight / 3, paint.also {
                it.style = Paint.Style.FILL
            }
        )
    }
}
