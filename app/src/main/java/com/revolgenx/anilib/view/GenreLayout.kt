package com.revolgenx.anilib.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.flexbox.FlexboxLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R

class GenreLayout(context: Context, attributeSet: AttributeSet?) :
    FlexboxLayout(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    fun addGenre(genres: List<String>, listener: ((genre: String) -> Unit)? = null) {
        genres.forEach { genre ->
            DynamicTextView(context).also { tv ->
                tv.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                tv.typeface = ResourcesCompat.getFont(context, R.font.open_sans_regular)
                tv.text = genre
                tv.textSize = 10f
                tv.setOnClickListener {
                    listener?.invoke(genre)
                }
            }
        }
    }
}