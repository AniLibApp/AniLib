package com.revolgenx.anilib.ui.view.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.pranavpandey.android.dynamic.support.Dynamic
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R

class AlSpinnerPreference: DynamicSpinnerPreference {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

    }

    override fun onInflate() {
        super.onInflate()
        Dynamic.setVisibility(iconView, View.GONE)
        (valueView as? DynamicTextView)?.colorType = Theme.ColorType.TEXT_PRIMARY
        val downImageView = DynamicImageView(context).apply {
            setImageResource(R.drawable.ic_keyboard_arrow_down)
            colorType = Theme.ColorType.TEXT_PRIMARY
        }
        setViewFrame(downImageView, true)
    }
}