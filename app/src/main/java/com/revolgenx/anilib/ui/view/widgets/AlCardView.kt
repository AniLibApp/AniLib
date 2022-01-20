package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView
import com.revolgenx.anilib.app.theme.ThemeController

open class AlCardView : DynamicCardView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun initialize() {
        super.initialize()
        cardElevation = ThemeController.elevaton.toFloat()
    }

}