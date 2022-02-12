package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.pranavpandey.android.dynamic.support.widget.DynamicSpinner
import com.revolgenx.anilib.util.onItemSelected

class AlSpinnerLayout : AlCardView {
    val spinnerView: DynamicSpinner = DynamicSpinner(context)
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        addView(spinnerView)
    }
}