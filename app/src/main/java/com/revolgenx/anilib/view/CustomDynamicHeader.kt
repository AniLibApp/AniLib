package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.view.DynamicHeader
import com.revolgenx.anilib.R

class CustomDynamicHeader(context: Context, attributeSet: AttributeSet?, defAttSet:Int): DynamicHeader(context, attributeSet, defAttSet) {
    constructor(context: Context):this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet,0){
        titleView.typeface = ResourcesCompat.getFont(context, R.font.open_sans_regular)
        subtitleView.typeface = ResourcesCompat.getFont(context, R.font.open_sans_regular)
    }
}