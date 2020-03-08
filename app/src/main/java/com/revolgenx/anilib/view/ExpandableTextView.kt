package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R

class ExpandableTextView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicTextView(context, attributeSet, style), View.OnClickListener {


    var myMaxLines = Integer.MAX_VALUE
        private set

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {

    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        /* If text longer than MAX_LINES set DrawableBottom - I'm using '...' ic_launcher */
        post {
            if (lineCount > MAX_LINES)
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ads_ic_more_horizontal)?.apply {
                        this.setTint(DynamicTheme.getInstance().get().tintPrimaryColor)
                    })
            else
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            maxLines = MAX_LINES
        }
    }

    override fun setMaxLines(maxLines: Int) {
        myMaxLines = maxLines
        super.setMaxLines(maxLines)
    }

    override fun onClick(v: View) {
        /* Toggle between expanded collapsed states */
        maxLines = if (myMaxLines == Integer.MAX_VALUE)
            MAX_LINES
        else
            Integer.MAX_VALUE
    }

    companion object {
        private const val MAX_LINES = 5
    }
}