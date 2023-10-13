package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.component.text.LargeSemiBoldText
import com.revolgenx.anilib.common.ui.model.HeaderModel

@Composable
fun HeaderBox(
    modifier: Modifier = Modifier,
    header: HeaderModel
) {
    HeaderBox(
        modifier = modifier,
        text = header.title ?: stringResource(id = header.titleRes!!)
    )
}

@Composable
fun HeaderBox(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 14.dp)
    ) {
        HeaderText(
            text = text
        )
    }
}


@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String
) {
    LargeSemiBoldText(
        modifier = modifier,
        text = text,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.3.sp,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        maxLines = 1,
    )
}