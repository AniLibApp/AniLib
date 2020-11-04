package com.revolgenx.anilib.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageButton
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class ExpandableTextViewLayout(context: Context, attributeSet: AttributeSet?, att: Int) :
    LinearLayout(context, attributeSet, att) {

    lateinit var expandableTextView: ExpandableTextView
    lateinit var expandableImageView: DynamicImageButton

    private val arrowUpDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_arrow_up)?.apply {
            setTint(DynamicTheme.getInstance().get().tintSurfaceColor)
        }
    }

    private val arrowDownDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)?.apply {
            setTint(DynamicTheme.getInstance().get().tintSurfaceColor)
        }
    }

    var isExpanded = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        orientation = VERTICAL
        var maxLines = 2
        attributeSet?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.ExpandableTextViewLayout, 0, 0)
                .apply {
                    maxLines = getInt(R.styleable.ExpandableTextViewLayout_maxLines, 1)
                }
        }

        expandableTextView = ExpandableTextView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            it.maxLines = maxLines
        }

        expandableImageView = DynamicImageButton(context, attributeSet).also {
            it.layoutParams = LayoutParams(dp(40f), dp(20f))
            it.setImageDrawable(arrowDownDrawable)
            it.setBackgroundColor(DynamicTheme.getInstance().get().surfaceColor)
        }

        expandableImageView.setOnClickListener {
            isExpanded = expandableTextView.toggle()
        }

        addView(expandableTextView)
        addView(expandableImageView)
    }

}