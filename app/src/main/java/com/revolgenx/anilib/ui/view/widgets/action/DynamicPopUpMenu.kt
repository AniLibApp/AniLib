package com.revolgenx.anilib.ui.view.widgets.action


import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu


class DynamicPopUpMenu : DynamicImageView {

    private var entries: Array<String>
    private var icons: IntArray? = null

    var onPopupMenuClickListener: ((parent:View, position: Int) -> Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicPopUpMenu)

        try {
            entries = a.getTextArray(R.styleable.DynamicPopUpMenu_entries).map{it.toString()}.toTypedArray()
            val resourceId = a.getResourceId(R.styleable.DynamicPopUpMenu_icons, -1)
            if (resourceId != -1) {
                val obtainTypedArray = resources.obtainTypedArray(resourceId)
                try {
                    val iconLength = obtainTypedArray.length()
                    icons = IntArray(iconLength)
                    for (i in 0 until iconLength) {
                        icons!![i] = obtainTypedArray.getResourceId(i, 0)
                    }

                } finally {
                    obtainTypedArray.recycle()
                }
            }
        } finally {
            a.recycle()
        }

        setOnClickListener {
            showPopup(this)
        }

    }

    private fun showPopup(anchor: View) {
        makeArrayPopupMenu(anchor, entries, icons){parent, _, position, _ ->
            onPopupMenuClickListener?.invoke(parent, position)
        }
    }
}