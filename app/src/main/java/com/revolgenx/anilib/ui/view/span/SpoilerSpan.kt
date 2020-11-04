package com.revolgenx.anilib.ui.view.span

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme

open class SpoilerSpan() : ClickableSpan() {

    var shown: Boolean = false

    override fun onClick(widget: View) {
        if (shown) return
        shown = true
        widget.invalidate()
    }

    override fun updateDrawState(ds: TextPaint) {
        if (shown) {
            ds.color = DynamicTheme.getInstance().get().tintSurfaceColor
            ds.bgColor = DynamicTheme.getInstance().get().surfaceColor
        } else {
            val accent = DynamicTheme.getInstance().get().tintAccentColor
            ds.color = accent
            ds.bgColor = accent
        }
    }
}