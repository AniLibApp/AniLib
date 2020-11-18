package com.revolgenx.anilib.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class ImageHeaderLayout : DynamicLinearLayout {

    var title: String? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ImageHeaderLayout,
            0, 0
        )
        val srcId = a.getResourceId(R.styleable.ImageHeaderLayout_headerImage, -1)

        orientation = LinearLayout.VERTICAL
        title = a.getString(R.styleable.ImageHeaderLayout_title)
        val iv = DynamicImageView(context).also {
            it.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).also {params->
                    params.gravity = Gravity.CENTER
                }
            it.setImageResource(srcId)
            it.color = DynamicTheme.getInstance().get().tintAccentColor
        }
        val tv = DynamicTextView(context).also {
            it.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).also {params->
                    params.gravity = Gravity.CENTER
                    params.topMargin = dp(4f)
                }
            it.text = title
        }
        addView(iv)
        addView(tv)
        a.recycle()
    }

}