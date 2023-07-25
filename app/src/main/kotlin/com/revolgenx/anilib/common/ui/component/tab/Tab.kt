package com.revolgenx.anilib.common.ui.component.tab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.ui.theme.onSurface
import com.revolgenx.anilib.common.ui.theme.primary

@Composable
fun Tab(
    selected: Boolean = false,
    onClick: ()->Unit,
    content: @Composable () -> Unit
) {
    Tab(
        selectedContentColor = primary,
        unselectedContentColor = onSurface,
        selected = selected,
        onClick = onClick
    ) {
        content()
    }
}