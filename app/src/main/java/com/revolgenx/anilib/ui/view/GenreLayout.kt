package com.revolgenx.anilib.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R

typealias GenreCallback = ((genre: String) -> Unit)?

class GenreLayout(context: Context, attributeSet: AttributeSet?) :
    LinearLayout(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    private var genreCallback: GenreCallback = null
    private val viewCache by lazy {
        mutableMapOf<Int, DynamicTextView>()
    }

    init {
        for (i in 0..4) {
            DynamicTextView(context).also { tv ->
                tv.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).also { params ->
                    params.rightMargin = DynamicUnitUtils.convertDpToPixels(6f)
                }
                tv.maxLines = 1
                tv.ellipsize = TextUtils.TruncateAt.END
                tv.typeface = ResourcesCompat.getFont(context, R.font.cabin_regular)
                tv.textSize = 10f
                tv.colorType = Theme.ColorType.ACCENT
                addView(tv)
                viewCache[i] = tv
                tv.setOnClickListener {
                    tv.text.toString().trim().takeIf { it.isNotEmpty() }?.let {
                        genreCallback?.invoke(it)
                    }
                }
            }
        }
    }

    fun addGenre(genres: List<String>?, listener: GenreCallback) {
        genreCallback = listener
        viewCache.forEach { it.value.text = "" }
        genres?.forEachIndexed { index, genre ->
            viewCache[index]?.text = genre
        }
    }

    fun clearCache() {
        viewCache.clear()
    }
}