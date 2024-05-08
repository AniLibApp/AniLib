package com.revolgenx.anilib.setting.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun GroupPreferenceItem(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        GroupPreferenceHeader(title = title)
        content()
    }
}

@Composable
private fun GroupPreferenceHeader(title: String) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 14.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = PrefsHorizontalPadding),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

