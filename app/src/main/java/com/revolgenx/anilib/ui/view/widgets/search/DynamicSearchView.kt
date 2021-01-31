package com.revolgenx.anilib.ui.view.widgets.search

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme

class DynamicSearchView : SearchView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val dynamicTheme = DynamicTheme.getInstance().get()

        val searchTv = findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        searchTv.setTextColor(dynamicTheme.textPrimaryColor)
        searchTv.textSize = 14f

        val searchButton = findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchButton.setColorFilter(dynamicTheme.textPrimaryColor, PorterDuff.Mode.SRC)

        val closeButton = findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeButton.setColorFilter(dynamicTheme.textPrimaryColor,PorterDuff.Mode.SRC)
    }
}