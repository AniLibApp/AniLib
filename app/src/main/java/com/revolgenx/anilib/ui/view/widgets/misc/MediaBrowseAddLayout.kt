package com.revolgenx.anilib.ui.view.widgets.misc

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.revolgenx.anilib.ui.view.widgets.AlCardView
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.app.theme.isEnoughWhite

class MediaBrowseAddLayout:AlCardView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        setBackgroundColor(dynamicAccentColor)
    }

    fun updateChildTheme(){
        (getChildAt(0) as ViewGroup).children.iterator().forEach {
            if(it is DynamicTextView){
                it.contrastWithColorType = Theme.ColorType.NONE
                it.backgroundAware =Theme.BackgroundAware.DISABLE
                it.color = if(isEnoughWhite(dynamicAccentColor)) Color.BLACK else Color.WHITE
            }

            if(it is DynamicImageView){
                it.contrastWithColorType = Theme.ColorType.NONE
                it.backgroundAware =Theme.BackgroundAware.DISABLE
                it.color = if(isEnoughWhite(dynamicAccentColor)) Color.BLACK else Color.WHITE
            }
        }
    }
}