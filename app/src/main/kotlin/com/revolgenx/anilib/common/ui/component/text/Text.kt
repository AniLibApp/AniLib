package com.revolgenx.anilib.common.ui.component.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun LargeBodySemiBoldText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    maxLines: Int? = null,
    lineHeight: TextUnit? = null,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
    letterSpacing: TextUnit? = null,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines ?: 2,
        fontSize = fontSize ?: 16.sp,
        lineHeight = lineHeight ?: 18.sp,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = letterSpacing ?: 0.2.sp,
        textAlign = textAlign,
        color = color,
        style = style
    )
}

@Composable
fun LargeBodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int? = null,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    MediumText(
        text = text,
        modifier = modifier,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        maxLines = maxLines,
        style = style,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    maxLines: Int? = null,
    lineHeight: TextUnit? = null,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines ?: 2,
        fontSize = fontSize ?: 13.sp,
        lineHeight = lineHeight ?: 16.sp,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp,
        textAlign = textAlign,
        color = color,
        style = style
    )
}


@Composable
fun SmallRegularText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    maxLines: Int? = null,
    lineHeight: TextUnit? = null,
    textAlign: TextAlign? = null,
    color: Color? = null,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines ?: 2,
        fontSize = fontSize ?: 12.sp,
        lineHeight = lineHeight ?: 13.sp,
        overflow = TextOverflow.Ellipsis,
        letterSpacing = 0.2.sp,
        textAlign = textAlign,
        color = color ?: MaterialTheme.colorScheme.onSurfaceVariant,
        style = style
    )
}


@Composable
fun SmallLightText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    maxLines: Int? = null,
    lineHeight: TextUnit? = null,
    textAlign: TextAlign? = null,
    color: Color? = null
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines ?: 1,
        fontSize = fontSize ?: 11.sp,
        lineHeight = lineHeight ?: 12.sp,
        color = color ?: MaterialTheme.colorScheme.onSurfaceVariant,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Light,
        letterSpacing = 0.2.sp,
        textAlign = textAlign
    )
}


fun TextStyle.shadow(color: Color = Color.Black) = copy(
    shadow = Shadow(
        color = color,
        offset = Offset(2.0f, 2.0f),
        blurRadius = 1f
    )
)