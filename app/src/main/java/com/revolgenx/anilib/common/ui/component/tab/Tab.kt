package com.revolgenx.anilib.common.ui.component.tab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable

@Composable
fun Tab(
    selected: Boolean = false,
    onClick: ()->Unit,
    content: @Composable () -> Unit
) {
    Tab(
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
        selected = selected,
        onClick = onClick
    ) {
        content()
    }
}