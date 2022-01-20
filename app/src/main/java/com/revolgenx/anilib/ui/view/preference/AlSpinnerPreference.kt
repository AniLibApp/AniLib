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

class AlSpinnerPreference : DynamicSpinnerPreference {

    private var showIcon = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun onLoadAttributes(attrs: AttributeSet?) {
        super.onLoadAttributes(attrs)
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.AlSpinnerPreference
        )
        try {
            showIcon = a.getBoolean(R.styleable.AlSpinnerPreference_al_show_icon, false)
        } finally {
            a.recycle()
        }
    }

    override fun onInflate() {
        super.onInflate()
        if(!showIcon){
            Dynamic.setVisibility(iconView, View.GONE)
        }
        (valueView as? DynamicTextView)?.colorType = Theme.ColorType.TEXT_PRIMARY

        val downImageView = DynamicImageView(context).apply {
            setImageResource(R.drawable.ic_keyboard_arrow_down)
            colorType = Theme.ColorType.TEXT_PRIMARY
        }
        setViewFrame(downImageView, true)
    }
}