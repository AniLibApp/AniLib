package com.revolgenx.anilib.common.ui.component.text

import android.text.Spanned
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.theme.onBackground
import com.revolgenx.anilib.social.factory.markdown

@Composable
fun MarkdownText(
    modifier: Modifier = Modifier,
    text: String? = null,
    spanned: Spanned? = null,
    color: Color = onBackground,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val textSize = if (fontSize.isSpecified) {
        with(LocalDensity.current) {
            fontSize.toPx()
        }
    } else 0f

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val textView = TextView(context).also {
                it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                it.typeface = ResourcesCompat.getFont(context, R.font.overpass_regular)
                it.letterSpacing = 0.02f
            }
            textView.setTextColor(color.toArgb())
            if (fontSize.isSpecified) {
                textView.textSize = textSize
            }
            if (text.isNullOrBlank().not()) {
                markdown.setMarkdown(textView, text!!)
            }
            if (spanned.isNotNull()) {
                markdown.setParsedMarkdown(textView, spanned!!)
            }
            textView
        },
        update = { textView ->
            if (text.isNullOrBlank().not()) {
                markdown.setMarkdown(textView, text!!)
            }
            if (spanned.isNotNull()) {
                markdown.setParsedMarkdown(textView, spanned!!)
            }
        }
    )
}