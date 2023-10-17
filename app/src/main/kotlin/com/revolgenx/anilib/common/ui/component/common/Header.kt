package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.component.text.SemiBoldText
import com.revolgenx.anilib.common.ui.model.HeaderModel

@Composable
fun HeaderBox(
    modifier: Modifier = Modifier,
    header: HeaderModel
) {
    HeaderBox(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 22.dp, bottom = 14.dp),
        text = header.title ?: stringResource(id = header.titleRes!!)
    )
}

@Composable
fun HeaderBox(text: String) {
    HeaderBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .padding(top = 22.dp, bottom = 14.dp),
        text = text
    )
}

@Composable
fun HeaderBox(
    modifier: Modifier,
    text: String
) {
    Box(
        modifier = modifier
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
    SemiBoldText(
        modifier = modifier,
        text = text,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.3.sp,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        maxLines = 1,
    )
}