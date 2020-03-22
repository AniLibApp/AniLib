package com.revolgenx.anilib.view.span

import android.content.Context
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme

abstract class SpoilerSpan(private val context: Context) : ClickableSpan() {

    var shown: Boolean = false

    override fun onClick(widget: View) {
        shown = true
        widget.invalidate()
    }

    override fun updateDrawState(ds: TextPaint) {
        val accent = DynamicTheme.getInstance().get().tintAccentColor

        if (shown) {
            ds.color = accent
            ds.bgColor = Color.TRANSPARENT
        } else {
            ds.color = accent
            ds.bgColor = accent
        }
    }
}