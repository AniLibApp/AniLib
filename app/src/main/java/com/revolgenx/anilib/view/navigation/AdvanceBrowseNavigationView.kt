package com.revolgenx.anilib.view.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.advance_browse_navigation_view.view.*


class AdvanceBrowseNavigationView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicNavigationView(context, attributeSet, style) {

    private val rView by lazy {
        LayoutInflater.from(context).inflate(
            R.layout.advance_browse_navigation_view,
            null,
            false
        )
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        addView(
            rView
        )

        rView.advanceNavigationSearchInputLayout.apply {
            this.setEndIconTintList(ColorStateList.valueOf( DynamicTheme.getInstance().get().tintAccentColor))
            this.setStartIconTintList(ColorStateList.valueOf( DynamicTheme.getInstance().get().accentColorDark))
        }
    }

}