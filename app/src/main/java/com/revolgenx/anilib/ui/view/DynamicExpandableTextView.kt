package com.revolgenx.anilib.ui.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary

class DynamicExpandableTextView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicTextView(context, attributeSet, style), View.OnClickListener {

    private var currentMaxLines = Integer.MAX_VALUE
    private val isExpanded get() = currentMaxLines == Integer.MAX_VALUE

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        setOnClickListener(this)
    }


    private val moreIcon by lazy {
        DynamicDrawableUtils.colorizeDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_more_horiz
            ), dynamicTextColorPrimary
        )
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        post {
            if (lineCount > MAX_LINES) {
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    moreIcon
                )
            } else {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                maxLines = MAX_LINES
            }
        }
    }

    override fun onClick(v: View) {
        toggle()
    }

    fun toggle() {
        val newMaxLine = if (isExpanded)
            MAX_LINES
        else
            Integer.MAX_VALUE

        currentMaxLines = newMaxLine
        maxLines = currentMaxLines
    }


    companion object {
        private const val MAX_LINES = 5
    }
}