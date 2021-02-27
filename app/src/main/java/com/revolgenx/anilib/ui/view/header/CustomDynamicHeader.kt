package com.revolgenx.anilib.ui.view.header

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.view.DynamicHeader
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.copyToClipBoard
import com.revolgenx.anilib.util.sp

class CustomDynamicHeader(context: Context, attributeSet: AttributeSet?, defAttSet: Int) :
    DynamicHeader(context, attributeSet, defAttSet) {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomDynamicHeader)

        try{
            val titleAlignCenter = a.getBoolean(R.styleable.CustomDynamicHeader_titleAlignCenter, false)
            val subtitleAlignCenter = a.getBoolean(R.styleable.CustomDynamicHeader_subtitleAlignCenter, false)
            val titleTextSize = a.getDimension(R.styleable.CustomDynamicHeader_titleTextSize, sp(14f))
            val subtitleTextSize = a.getDimension(R.styleable.CustomDynamicHeader_subtitleTextSize, sp(13f))
            val copySubtitle = a.getBoolean(R.styleable.CustomDynamicHeader_copySubtitleOnLongClick, false)

            if(titleAlignCenter){
                (titleView as DynamicTextView).isRtlSupport = false
                titleView.textAlignment = TEXT_ALIGNMENT_CENTER
            }

            if(subtitleAlignCenter){
                (subtitleView as DynamicTextView).isRtlSupport = false
                subtitleView.textAlignment = TEXT_ALIGNMENT_CENTER
            }

            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
            subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtitleTextSize)

            if(copySubtitle){
                subtitleView.setOnLongClickListener {
                    subtitleView.text.trim().takeIf { it.isNotEmpty() }?.let {
                        context.copyToClipBoard(it.toString())
                        true
                    } ?: false
                }
            }

        }catch (_:Exception){

        }finally{
          a.recycle()
        }

        titleView.typeface = ResourcesCompat.getFont(context, R.font.cabin_regular)
        subtitleView.typeface = ResourcesCompat.getFont(context, R.font.cabin_regular)

        subtitleView.setTextColor(DynamicTheme.getInstance().get().textPrimaryColor)
    }
}