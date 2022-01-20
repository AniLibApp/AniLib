package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import com.pranavpandey.android.dynamic.support.view.base.DynamicItemView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.sp

class AniLibItemView : DynamicItemView {
    private var textAllCaps = false
    private var textSize = 12f
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun onLoadAttributes(attrs: AttributeSet?) {
        super.onLoadAttributes(attrs)
        val a = context.obtainStyledAttributes(attrs, R.styleable.AniLibItemView)
        try {
            textAllCaps = a.getBoolean(R.styleable.AniLibItemView_titleTextAllCaps, false);
            textSize = a.getDimension(R.styleable.AniLibItemView_titleTextSize, sp(14f))
        }finally {
            a.recycle()
        }
    }

    override fun onInflate() {
        super.onInflate()
        titleView?.isAllCaps = textAllCaps
        titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

}