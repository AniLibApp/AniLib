package com.revolgenx.anilib.ui.view.header

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.Dynamic
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.view.DynamicHeader
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.util.copyToClipBoard
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.sp

open class AlHeaderItemView(context: Context, attributeSet: AttributeSet?, defAttSet: Int) :
    DynamicHeader(context, attributeSet, defAttSet) {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.AlHeaderItemView)

        try {
            val titleAlignCenter =
                a.getBoolean(R.styleable.AlHeaderItemView_titleAlignCenter, false)
            val subtitleAlignCenter =
                a.getBoolean(R.styleable.AlHeaderItemView_subtitleAlignCenter, false)
            val titleTextSize = a.getDimension(R.styleable.AlHeaderItemView_titleTextSize, sp(14f))
            val subtitleTextSize =
                a.getDimension(R.styleable.AlHeaderItemView_subtitleTextSize, sp(13f))
            val copySubtitle =
                a.getBoolean(R.styleable.AlHeaderItemView_copySubtitleOnLongClick, false)
            val ellipsizeSubtitle =
                a.getBoolean(R.styleable.AlHeaderItemView_ellipsizeSubtitle, false)

            if (titleAlignCenter) {
                (titleView as DynamicTextView).isRtlSupport = false
                titleView?.textAlignment = TEXT_ALIGNMENT_CENTER
            }

            if (subtitleAlignCenter) {
                (subtitleView as DynamicTextView).isRtlSupport = false
                subtitleView?.textAlignment = TEXT_ALIGNMENT_CENTER
            }

            titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
            subtitleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtitleTextSize)

            titleView?.setPadding(0, 0, 0, dp(4f))
            if (copySubtitle) {
                subtitleView?.setOnLongClickListener {
                    subtitleView?.text?.trim()?.takeIf { it.isNotEmpty() }?.let {
                        context.copyToClipBoard(it.toString())
                        true
                    } ?: false
                }
            }

            if (ellipsizeSubtitle) {
                subtitleView?.ellipsize = TextUtils.TruncateAt.END
            }

        } catch (_: Exception) {

        } finally {
            a.recycle()
        }

        titleView?.typeface = ResourcesCompat.getFont(context, R.font.rubik_regular)
        subtitleView?.typeface = ResourcesCompat.getFont(context, R.font.rubik_regular)

    }

    override fun setColor() {
        super.setColor()
        Dynamic.setColorType(subtitleView, Theme.ColorType.TEXT_PRIMARY);
    }
}