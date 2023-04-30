package com.revolgenx.anilib.common.ui.view

import android.text.Spanned
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
import com.revolgenx.anilib.common.ext.isNotNull
import com.revolgenx.anilib.social.factory.markwon

@Composable
fun MarkdownTextView(
    modifier: Modifier = Modifier,
    text: String? = null,
    spanned: Spanned? = null,
    color: Color = MaterialTheme.colorScheme.onBackground,
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
            val textView = TextView(context)
            textView.setTextColor(color.toArgb())
            if (fontSize.isSpecified) {
                textView.textSize = textSize
            }
            if (text.isNullOrBlank().not()) {
                markwon.setMarkdown(textView, text!!)
            }
            if (spanned.isNotNull()) {
                markwon.setParsedMarkdown(textView, spanned!!)
            }
            textView
        },
        update = { textView ->
            if (text.isNullOrBlank().not()) {
                markwon.setMarkdown(textView, text!!)
            }
            if (spanned.isNotNull()) {
                markwon.setParsedMarkdown(textView, spanned!!)
            }
        }
    )
}