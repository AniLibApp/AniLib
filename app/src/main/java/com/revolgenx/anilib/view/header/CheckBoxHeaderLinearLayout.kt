package com.revolgenx.anilib.view.header

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp


class CheckBoxHeaderLinearLayout(context: Context, attributeSet: AttributeSet?, defAttr: Int) :
    DynamicLinearLayout(context, attributeSet, defAttr) {

    lateinit var headerCheckBox: DynamicCheckBox

    var checkBoxHeaderName: String? = null
        set(value) {
            field = value
            headerCheckBox.text = value
        }

    var checked = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        orientation = LinearLayout.VERTICAL
        headerCheckBox = DynamicCheckBox(context, attributeSet).apply {
            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textSize = 11f
            this.setTextColor(DynamicTheme.getInstance().get().textPrimaryColorInverse)
            this.setPadding(dp(5f))
        }
        addView(headerCheckBox)
        headerCheckBox.setOnCheckedChangeListener { _, isChecked ->
            checked = isChecked
            getChildAt(1)?.isEnabled = isChecked
        }

        attributeSet?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.CheckBoxHeaderLinearLayout, 0, 0)
                .apply {
                    checkBoxHeaderName =
                        getString(R.styleable.CheckBoxHeaderLinearLayout_checkBoxHeaderName) ?: ""
                }
        }
    }
}