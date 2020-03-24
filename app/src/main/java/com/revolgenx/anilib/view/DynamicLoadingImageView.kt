package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView


class DynamicLoadingImageView(
    context: Context,
    attributeSet: AttributeSet?,
    defStyle: Int
) : DynamicImageView(context, attributeSet, defStyle) {

    companion object {
        const val androidSchemas = "http://schemas.android.com/apk/res/android"
    }

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(context).also { it.start() }
    }

    private var srcId = 0
    private var loading: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        srcId = attributeSet?.getAttributeResourceValue(androidSchemas, "src", -1) ?: srcId
    }

    override fun setImageResource(resId: Int) {
        this.srcId = resId
        super.setImageResource(resId)
    }

    fun showLoading(b: Boolean) {
        if (b) {
            setImageDrawable(circularProgressDrawable)
        } else {
            setImageResource(srcId)
        }
    }

    fun toggleLoading() {
        if (loading) {
            setImageResource(srcId)
        } else {
            setImageDrawable(circularProgressDrawable)
        }
        loading = !loading
    }

}