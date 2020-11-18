package com.revolgenx.anilib.ui.view.header

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class DynamicHeaderLinearLayout(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    DynamicLinearLayout(context, attributeSet, defStyleAttr) {

    lateinit var headerTextView: DynamicTextView

    var headerName: String? = null
        set(value) {
            field = value
            headerTextView.text = value
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        orientation = LinearLayout.VERTICAL
        headerTextView = DynamicTextView(context, attributeSet).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textSize = 11f
            this.setPadding(dp(5f))
        }
        addView(headerTextView)

        attributeSet?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.DynamicHeaderLinearLayout, 0, 0)
                .apply {
                    headerName = getString(R.styleable.DynamicHeaderLinearLayout_headerName) ?: ""
                }
        }

    }


}