package com.revolgenx.anilib.ui.view

import android.content.Context
import android.util.AttributeSet
import com.github.ybq.android.spinkit.SpinKitView
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme

class CustomSpinKit : SpinKitView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {
        this.setColor(DynamicTheme.getInstance().get().tintPrimaryColor)
    }
}