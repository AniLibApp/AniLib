package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> Grid(
    modifier: Modifier = Modifier,
    items: List<T>,
    columns: Int = 2,
    rowSpacing: Dp = 2.dp,
    columnSpacing: Dp = 2.dp,
    itemView: @Composable (T) -> Unit,
) {
    val rows = items.chunked(columns)
    val extraSpacings = rows.size.minus(1).times(rowSpacing.value)

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val itemHeight = Dp(maxHeight.value.minus(extraSpacings).div(rows.size))
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.heightIn(max = itemHeight),
                    horizontalArrangement = Arrangement.spacedBy(columnSpacing),
                ) {
                    rowItems.forEach { item ->
                        Column(
                            modifier = Modifier.weight(1f, true),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = { itemView(item) }
                        )
                    }
                    // Add a filler column if the row has less columns than the total number of columns
                    if (rowItems.size < columns) {
                        repeat(columns - rowItems.size) {
                            Column(
                                modifier = Modifier.weight(1f, true),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {}
                        }
                    }
                }
            }
        }
    }
}