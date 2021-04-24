package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import com.pranavpandey.android.dynamic.support.view.DynamicItemView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.sp

class AniLibItemView : DynamicItemView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.AniLibItemView,
            defStyle,
            0
        )
        try {
            titleView.isAllCaps = a.getBoolean(R.styleable.AniLibItemView_titleTextAllCaps, false);
            val titleTextSize = a.getDimension(R.styleable.AniLibItemView_titleTextSize, sp(14f))
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        } catch (ex: Exception) {
        } finally {
            a.recycle()
        }
    }
}