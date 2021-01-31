package com.revolgenx.anilib.ui.view.header

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class HorizontalHeaderLayout : DynamicLinearLayout {

    private val titleView: TextView
    private val subtitleView: TextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        orientation = HORIZONTAL

        titleView = DynamicTextView(context)
            .also {
                it.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                it.typeface = ResourcesCompat.getFont(context, R.font.cabin_medium)
                it.textSize = 11f
                it.setPadding(dp(8f))
                addView(it)
            }

        subtitleView = DynamicTextView(context)
            .also {
                it.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                it.typeface = ResourcesCompat.getFont(context, R.font.cabin_medium)
                it.colorType = Theme.ColorType.ACCENT
                it.textSize = 11f
                it.setPadding(dp(8f))
                addView(it)
            }

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalHeaderLayout)

        try {
            titleView.text = a.getString(R.styleable.HorizontalHeaderLayout_titleText)
            subtitleView.text = a.getString(R.styleable.HorizontalHeaderLayout_subtitleText)
        } catch (e: Exception) {
        } finally {
            a.recycle()
        }
    }


    var title: String = ""
        set(value) {
            field = value
            titleView.text = value
        }

    var subtitle: String = ""
        set(value) {
            field = value
            subtitleView.text = value
        }

}