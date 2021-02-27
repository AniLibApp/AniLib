package com.revolgenx.anilib.ui.view

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary

class DynamicExpandableTextView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicTextView(context, attributeSet, style), View.OnClickListener {

    var myMaxLines = Integer.MAX_VALUE

    private var expansionListener:((Boolean)->Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        setOnClickListener(this)
    }


    private val moreIcon by lazy {
        DynamicDrawableUtils.colorizeDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ads_ic_more_horizontal
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
            if (lineCount > MAX_LINES){
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    moreIcon
                )
            }
            else{
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                maxLines = MAX_LINES
            }
        }
    }


    override fun setMaxLines(maxLines: Int) {
        myMaxLines = maxLines
        super.setMaxLines(maxLines)
    }

    override fun onClick(v: View) {
        /* Toggle between expanded collapsed states */
        toggle()
    }

    fun toggle() {
        maxLines = if (isExpanded())
            MAX_LINES
        else
            Integer.MAX_VALUE

        expansionListener?.invoke(isExpanded())
    }

    fun isExpanded(): Boolean {
        return myMaxLines == Integer.MAX_VALUE
    }

    fun needsExpansion(): Boolean {
        return lineCount > MAX_LINES
    }

    fun expansionListener(listener: (Boolean) -> Unit) {
        expansionListener = listener
    }

    companion object {
        private const val MAX_LINES = 5
    }
}