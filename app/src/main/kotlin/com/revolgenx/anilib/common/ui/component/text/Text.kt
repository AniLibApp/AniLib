package com.revolgenx.anilib.common.ui.component.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ext.colorScheme

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    maxLines: Int? = null,
    lineHeight: TextUnit? = null,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines ?: 2,
        fontSize = fontSize ?: 13.sp,
        lineHeight = lineHeight ?: 14.sp,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp,
        textAlign = textAlign,
        color = color
    )
}


@Composable
fun LightText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    maxLines: Int? = null,
    lineHeight: TextUnit? = null,
    textAlign: TextAlign? = null,
    color: Color? = null,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines ?: 1,
        fontSize = fontSize ?: 11.sp,
        lineHeight = lineHeight ?: 12.sp,
        color = color ?: colorScheme().onSurfaceVariant,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Light,
        letterSpacing = 0.2.sp,
        textAlign = textAlign
    )
}
