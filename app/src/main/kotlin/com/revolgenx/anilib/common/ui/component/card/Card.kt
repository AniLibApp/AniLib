package com.revolgenx.anilib.common.ui.component.card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnLongClick

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Card(
    modifier: Modifier = Modifier,
    onClick: OnClick? = null,
    onLongClick: OnLongClick? = null,
    colors: CardColors = CardDefaults.cardColors(
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = colors,
    ) {
        Column(
            modifier = Modifier.let {
                if (onClick != null) {
                    it.combinedClickable(onLongClick = onLongClick, onClick = onClick)
                } else {
                    it
                }
            },
            content = content
        )
    }
}