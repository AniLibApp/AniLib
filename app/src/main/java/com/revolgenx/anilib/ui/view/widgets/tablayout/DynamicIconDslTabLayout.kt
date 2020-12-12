package com.revolgenx.anilib.ui.view.widgets.tablayout

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import com.angcyo.tablayout.DslTabLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.revolgenx.anilib.R

class DynamicIconDslTabLayout : DslTabLayout {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()
    val iconMenu = mutableListOf<DynamicImageView>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicIconDslTabLayout)
        var icons: IntArray? = null

        try {
            val resourceId = a.getResourceId(R.styleable.DynamicIconDslTabLayout_icons, -1)
            if (resourceId != -1) {
                val obtainTypedArray = resources.obtainTypedArray(resourceId)
                try {
                    val iconLength = obtainTypedArray.length()
                    icons = IntArray(iconLength)
                    for (i in 0 until iconLength) {
                        icons[i] = obtainTypedArray.getResourceId(i, 0)
                    }

                } finally {
                    obtainTypedArray.recycle()
                }
            }
        } finally {
            a.recycle()
        }


        configTabLayoutConfig {
            this.tabSelectColor = dynamicTheme.accentColor
            this.tabDeselectColor = dynamicTheme.textPrimaryColor
        }

        icons?.forEach {
            val dynamicImageView =
                DynamicImageView(ContextThemeWrapper(context, R.style.DslTabLayoutIconStyle))
            dynamicImageView.setImageResource(it)
            addView(dynamicImageView)
            iconMenu.add(dynamicImageView)
        }

        itemIsEquWidth = true

    }

}
