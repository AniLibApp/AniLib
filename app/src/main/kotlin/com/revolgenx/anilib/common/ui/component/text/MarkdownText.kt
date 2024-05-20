package com.revolgenx.anilib.common.ui.component.text

import android.content.Context
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.social.factory.markdown


class MarkdownTextView(context: Context, attributeSet: AttributeSet?, style: Int) :
    AppCompatTextView(context, attributeSet, style) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        typeface = ResourcesCompat.getFont(context, R.font.overpass_regular)
        letterSpacing = 0.02f
    }

    override fun scrollTo(x: Int, y: Int) {
    }

}

@Composable
fun MarkdownText(
    modifier: Modifier = Modifier,
    text: String? = null,
    spanned: Spanned? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onLineCount: ((Int) -> Unit)? = null,
    onClick: OnClick? = null
) {
    val textSize = with(LocalDensity.current) {
        if (fontSize.isSpecified) {
            fontSize.toPx()
        } else {
            LocalTextStyle.current.fontSize.toPx()
        }
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            MarkdownTextView(context).also { textView ->
                textView.maxLines = maxLines
                textView.ellipsize = TextUtils.TruncateAt.END
                onClick?.let { c ->
                    textView.setOnClickListener {
                        c.invoke()
                    }
                }
                textView.setTextColor(color.toArgb())

                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                if (text.isNullOrBlank().not()) {
                    markdown.setMarkdown(textView, text!!)
                } else if (spanned.isNotNull()) {
                    markdown.setParsedMarkdown(textView, spanned!!)
                }

                onLineCount?.let {
                    textView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                        override fun onLayoutChange(
                            v: View?,
                            left: Int,
                            top: Int,
                            right: Int,
                            bottom: Int,
                            oldLeft: Int,
                            oldTop: Int,
                            oldRight: Int,
                            oldBottom: Int
                        ) {
                            textView.removeOnLayoutChangeListener(this);
                            onLineCount.invoke(textView.lineCount);
                        }
                    })
                }
            }
        },
        update = { textView ->
            textView.maxLines = maxLines

            if (text.isNullOrBlank().not()) {
                markdown.setMarkdown(textView, text!!)
            } else if (spanned.isNotNull()) {
                markdown.setParsedMarkdown(textView, spanned!!)
            }
        }
    )
}