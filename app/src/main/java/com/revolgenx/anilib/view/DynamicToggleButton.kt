package com.revolgenx.anilib.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.view.setPadding
import androidx.core.widget.ImageViewCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.revolgenx.anilib.util.dp

typealias CheckListener = ((checked: Boolean) -> Unit)?

class DynamicToggleButton(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicImageView(context, attributeSet, style) {

    private val accentColor by lazy { DynamicTheme.getInstance().get().accentColor }
    private val primaryTint by lazy { DynamicTheme.getInstance().get().tintPrimaryColor }

    private var checkListener: CheckListener = null

    var checked = false
        set(value) {
            field = value
            ImageViewCompat.setImageTintList(
                this,
                ColorStateList.valueOf(if (field) accentColor else primaryTint)
            )
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        this.setOnClickListener {
            checked = !checked

            checkListener?.invoke(checked)
        }
        setPadding(dp(10f))
        checked = false
    }

    fun setToggleListener(listener: CheckListener) {
        checkListener = listener
    }
}