package com.revolgenx.anilib.user.view

import android.content.Context
import android.util.AttributeSet
import com.revolgenx.anilib.ui.view.header.AlHeaderItemView

class UserHeaderItemView : AlHeaderItemView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        loadAttributes()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleSet: Int) : super(
        context,
        attributeSet,
        defStyleSet
    ) {
        loadAttributes()
    }

    private fun loadAttributes() {
        itemView.setPadding(itemView.paddingLeft, itemView.paddingTop, itemView.paddingRight, 0)
    }

}