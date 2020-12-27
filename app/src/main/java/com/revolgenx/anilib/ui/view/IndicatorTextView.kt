package com.revolgenx.anilib.ui.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.util.dp

class IndicatorTextView : DynamicLinearLayout {

    private var indicator: View
    private var textView: DynamicTextView

    var text: String = ""
        set(value) {
            field = value
            textView.text = text
        }

    var status: Int? = null
        set(value) {
            field = value
            if (value == null) {
                indicator.visibility = View.GONE
            } else {
                indicator.visibility = View.VISIBLE
                val statusDrawable = when (status) {
                    MediaListStatus.CURRENT.ordinal -> {
                        R.drawable.ic_watching
                    }
                    MediaListStatus.PLANNING.ordinal->{
                        R.drawable.ic_planning
                    }
                    MediaListStatus.DROPPED.ordinal -> {
                        R.drawable.ic_dropped
                    }
                    MediaListStatus.COMPLETED.ordinal -> {
                        R.drawable.ic_completed
                    }
                    MediaListStatus.PAUSED.ordinal -> {
                        R.drawable.ic_paused_filled
                    }
                    MediaListStatus.REPEATING.ordinal -> {
                        R.drawable.ic_rewatching
                    }
                    else -> {
                        R.drawable.ads_ic_info
                    }
                }
                indicator.background =
                    DynamicDrawableUtils.colorizeDrawable(
                        ContextCompat.getDrawable(context, statusDrawable),
                        mediaListStatusColors[value]
                    )

            }
        }

    private val mediaListStatusColors by lazy {
        context.resources.getStringArray(R.array.media_list_status_color)
            .map { Color.parseColor(it) }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.IndicatorTextView,
            def,
            0
        )

        val textColorType: Int
        val indicatorTextSize: Float
        try {
            textColorType =
                a.getInt(R.styleable.IndicatorTextView_textColorType, Theme.ColorType.TEXT_PRIMARY)
            indicatorTextSize =
                a.getInt(R.styleable.IndicatorTextView_textSize, 10).toFloat()
        } finally {
            a.recycle()
        }

        orientation = HORIZONTAL
        indicator = View(context).apply {
            layoutParams = LayoutParams(dp(14f), dp(14f)).also {
                it.gravity = Gravity.CENTER_VERTICAL
                it.marginEnd = dp(4f)
            }
        }

        textView = DynamicTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                it.gravity = Gravity.CENTER_VERTICAL
            }
            colorType = textColorType
            textSize = indicatorTextSize
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }

        addView(indicator)
        addView(textView)
    }

}